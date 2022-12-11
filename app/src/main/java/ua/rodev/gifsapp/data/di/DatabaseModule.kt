package ua.rodev.gifsapp.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.rodev.gifsapp.data.cache.GifsDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext ctx: Context) =
        Room.databaseBuilder(ctx, GifsDatabase::class.java, "gifs-database")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideGifsDao(appDatabase: GifsDatabase) = appDatabase.gifsDao()

    @Singleton
    @Provides
    fun provideGifsRemoteKeysDao(appDatabase: GifsDatabase) = appDatabase.gifsRemoteKeysDao()
}
