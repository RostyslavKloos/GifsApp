package ua.rodev.gifsapp.data.pagging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import ua.rodev.gifsapp.data.GifData
import ua.rodev.gifsapp.data.cache.GifsDao
import ua.rodev.gifsapp.data.cache.GifsDatabase
import ua.rodev.gifsapp.data.cache.GifsRemoteKeysDao
import ua.rodev.gifsapp.data.cloud.GifsService
import ua.rodev.gifsapp.data.cloud.gifs.GifCloud
import ua.rodev.gifsapp.data.cloud.gifs.GifsCloud
import javax.inject.Inject

@ViewModelScoped
class GifsPagingRepository @Inject constructor(
    private val service: GifsService,
    private val gifsDao: GifsDao,
    private val database: GifsDatabase,
    private val gifsRemoteKeysDao: GifsRemoteKeysDao,
    private val withoutDeletedGifsMapper: GifsCloud.Mapper<List<GifCloud>>,
) {

    fun gifsFlow(query: String): Flow<PagingData<GifData>> {

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_LIMIT,
                initialLoadSize = PAGE_INITIAL_LIMIT,
                enablePlaceholders = false
            ),
            remoteMediator = GifsRemoteMediator(
                query,
                service,
                database,
                gifsDao,
                gifsRemoteKeysDao,
                withoutDeletedGifsMapper,
            ),
            pagingSourceFactory = { gifsDao.gifsPagingSourceByQuery(query) }
        ).flow
    }

    companion object {
        private const val PAGE_LIMIT = 15
        const val PAGE_INITIAL_LIMIT = 30
    }
}
