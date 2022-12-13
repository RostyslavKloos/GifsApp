package ua.rodev.gifsapp.data.pagging

import androidx.paging.*
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import ua.rodev.gifsapp.data.GifData
import ua.rodev.gifsapp.data.cache.GifsDao
import ua.rodev.gifsapp.data.cache.GifsDatabase
import ua.rodev.gifsapp.data.cache.GifsRemoteKeys
import ua.rodev.gifsapp.data.cache.GifsRemoteKeysDao
import ua.rodev.gifsapp.data.cloud.GifsService
import ua.rodev.gifsapp.data.cloud.gifs.GifCloud
import ua.rodev.gifsapp.data.cloud.gifs.GifsCloud
import ua.rodev.gifsapp.presentation.gifs.PagingException
import javax.inject.Inject

interface GifsPagingRepository {

    fun gifsFlow(query: String): Flow<PagingData<GifData>>

    @OptIn(ExperimentalPagingApi::class)
    class Main @Inject constructor(
        private val service: GifsService,
        private val gifsDao: GifsDao,
        private val database: GifsDatabase,
        private val gifsRemoteKeysDao: GifsRemoteKeysDao,
        private val withoutDeletedGifsMapper: GifsCloud.Mapper<List<GifCloud>>,
    ) : GifsPagingRepository {

        override fun gifsFlow(query: String): Flow<PagingData<GifData>> {

            return Pager(
                config = PagingConfig(
                    pageSize = PAGE_LIMIT,
                    initialLoadSize = PAGE_INITIAL_LIMIT,
                    enablePlaceholders = false
                ),
                remoteMediator = GifsRemoteMediator(
                    query,
                ),
                pagingSourceFactory = { gifsDao.gifsPagingSourceByQuery(query) }
            ).flow
        }

        @OptIn(ExperimentalPagingApi::class)
        inner class GifsRemoteMediator(
            private val query: String,
        ) : RemoteMediator<Int, GifData>() {

            override suspend fun initialize(): InitializeAction =
                InitializeAction.LAUNCH_INITIAL_REFRESH

            override suspend fun load(
                loadType: LoadType,
                state: PagingState<Int, GifData>,
            ): MediatorResult {
                val page = when (loadType) {
                    LoadType.REFRESH -> {
                        val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                        remoteKeys?.nextKey?.minus(1) ?: START_PAGE
                    }
                    LoadType.PREPEND -> {
                        val remoteKeys = getRemoteKeyForFirstItem(state)
                        remoteKeys?.prevKey
                            ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    }
                    LoadType.APPEND -> {
                        val remoteKeys = getRemoteKeyForLastItem(state)
                        remoteKeys?.nextKey
                            ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    }
                }

                val limit =
                    if (page == START_PAGE) state.config.initialLoadSize else state.config.pageSize
                val offset = if (page == START_PAGE) 0 else PAGE_INITIAL_LIMIT + (page - 1) * limit

                try {
                    val response = service.gifs(query = query, offset = offset, limit = limit)
                    if (response.isSuccessful()) {
                        val gifs = response.map(withoutDeletedGifsMapper)
                        val isLastPageReached = gifs.isEmpty()
                        database.withTransaction {
                            if (loadType == LoadType.REFRESH) {
                                gifsDao.deleteGifsByKeyword(query)
                                gifsRemoteKeysDao.deleteRemoteKeys()
                            }
                            val prevKey = if (page == START_PAGE) null else page - 1
                            val nextKey = if (isLastPageReached) null else page + 1
                            val keys = gifs.map {
                                it.toRemoteKey(prevKey, nextKey)
                            }
                            gifsRemoteKeysDao.insertAll(keys)
                            val mapper = GifCloud.Mapper.GifCloudToGifDataMapper(query)
                            val mappedGifs = gifs.map {
                                it.map(mapper)
                            }
                            gifsDao.insertAll(mappedGifs)
                        }
                        return MediatorResult.Success(endOfPaginationReached = isLastPageReached)
                    } else {
                        return MediatorResult.Error(PagingException.Error())
                    }
                } catch (e: Exception) {
                    return MediatorResult.Error(PagingException.NetworkError)
                }
            }

            private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, GifData>): GifsRemoteKeys? {
                return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
                    ?.let { gif ->
                        gifsRemoteKeysDao.remoteKeysGifId(gif.id)
                    }
            }

            private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, GifData>): GifsRemoteKeys? {
                return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
                    ?.let { gif ->
                        gifsRemoteKeysDao.remoteKeysGifId(gif.id)
                    }
            }

            private suspend fun getRemoteKeyClosestToCurrentPosition(
                state: PagingState<Int, GifData>,
            ): GifsRemoteKeys? {
                return state.anchorPosition?.let { position ->
                    state.closestItemToPosition(position)?.id?.let { id ->
                        gifsRemoteKeysDao.remoteKeysGifId(id)
                    }
                }
            }
        }

        companion object {
            private const val START_PAGE = 0
            private const val PAGE_LIMIT = 15
            private const val PAGE_INITIAL_LIMIT = 30
        }
    }
}
