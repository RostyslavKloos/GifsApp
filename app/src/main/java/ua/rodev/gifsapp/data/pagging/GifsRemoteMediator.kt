package ua.rodev.gifsapp.data.pagging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ua.rodev.gifsapp.data.GifData
import ua.rodev.gifsapp.data.cache.GifsDao
import ua.rodev.gifsapp.data.cache.GifsDatabase
import ua.rodev.gifsapp.data.cache.GifsRemoteKeys
import ua.rodev.gifsapp.data.cache.GifsRemoteKeysDao
import ua.rodev.gifsapp.data.cloud.GifsService
import ua.rodev.gifsapp.data.cloud.gifs.toGifsData
import ua.rodev.gifsapp.data.pagging.GifsPagingRepository.Companion.PAGE_INITIAL_LIMIT
import ua.rodev.gifsapp.presentation.gifs.PagingException

@OptIn(ExperimentalPagingApi::class)
class GifsRemoteMediator(
    private val query: String,
    private val service: GifsService,
    private val database: GifsDatabase,
    private val gifsDao: GifsDao,
    private val gifsRemoteKeysDao: GifsRemoteKeysDao,
) : RemoteMediator<Int, GifData>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

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

        val limit = if (page == START_PAGE) state.config.initialLoadSize else state.config.pageSize
        val offset = if (page == START_PAGE) 0 else PAGE_INITIAL_LIMIT + (page - 1) * limit

        try {
            val response = service.gifs(query = query, offset = offset, limit = limit)
            if (response.meta.status == 200) {
                val gifs = response.data
                val isLastPageReached = gifs.isEmpty()
                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        gifsRemoteKeysDao.deleteRemoteKeys()
                        gifsDao.deleteGifs()
                    }
                    val prevKey = if (page == START_PAGE) null else page - 1
                    val nextKey = if (isLastPageReached) null else page + 1
                    val keys = gifs.map {
                        GifsRemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                    }
                    gifsRemoteKeysDao.insertAll(keys)
                    gifsDao.insertAll(gifs.toGifsData(query))
                }
                return MediatorResult.Success(endOfPaginationReached = isLastPageReached)
            } else {
                // TODO refactor
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

    companion object {
        private const val START_PAGE = 0
    }
}
