package ua.rodev.gifsapp.data.cache

import android.content.Context
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import coil.memory.MemoryCache

interface DeleteGifUseCase {
    suspend fun delete(id: String, url: String)

    @OptIn(ExperimentalCoilApi::class)
    class Main(
        context: Context,
        private val gifsDao: GifsDao,
        private val deletedGifsDao: DeletedGifsDao,
        private val remoteKeysDao: GifsRemoteKeysDao,
    ) : DeleteGifUseCase {

        private val imageLoader = context.imageLoader

        override suspend fun delete(id: String, url: String) {
            imageLoader.memoryCache?.remove(MemoryCache.Key(url))
            imageLoader.diskCache?.remove(url)
            gifsDao.deleteById(id)
            remoteKeysDao.deleteRemoteKeyById(id)
            deletedGifsDao.insert(DeletedGif(id))
        }
    }
}
