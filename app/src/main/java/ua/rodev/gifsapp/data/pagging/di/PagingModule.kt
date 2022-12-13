package ua.rodev.gifsapp.data.pagging.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ua.rodev.gifsapp.data.cache.GifsDao
import ua.rodev.gifsapp.data.cache.GifsDatabase
import ua.rodev.gifsapp.data.cache.GifsRemoteKeysDao
import ua.rodev.gifsapp.data.cloud.GifsService
import ua.rodev.gifsapp.data.cloud.gifs.GifCloud
import ua.rodev.gifsapp.data.cloud.gifs.GifsCloud
import ua.rodev.gifsapp.data.pagging.GifsPagingRepository

@Module
@InstallIn(ViewModelComponent::class)
object PagingModule {

    @Provides
    @ViewModelScoped
    fun provideGifsPagingRepository(
        service: GifsService,
        gifsDao: GifsDao,
        gifsRemoteKeysDao: GifsRemoteKeysDao,
        database: GifsDatabase,
        withoutDeletedGifsMapper: GifsCloud.Mapper<List<GifCloud>>,
    ): GifsPagingRepository {
        return GifsPagingRepository.Main(
            service = service,
            gifsDao = gifsDao,
            gifsRemoteKeysDao = gifsRemoteKeysDao,
            database = database,
            withoutDeletedGifsMapper = withoutDeletedGifsMapper
        )
    }
}
