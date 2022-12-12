package ua.rodev.gifsapp.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rodev.gifsapp.data.cache.DeletedGifsDao
import ua.rodev.gifsapp.data.cloud.gifs.GifCloud
import ua.rodev.gifsapp.data.cloud.gifs.GifsCloud

@Module
@InstallIn(SingletonComponent::class)
object DataMappersModule {

    @Provides
    fun provideWithoutDeletedGifsMapper(
        deletedGifsDao: DeletedGifsDao
    ): GifsCloud.Mapper<List<GifCloud>> {
        return GifsCloud.Mapper.WithoutDeletedGifsMapper(deletedGifsDao)
    }
}
