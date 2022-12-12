package ua.rodev.gifsapp.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.rodev.gifsapp.core.CoroutineDispatchers
import ua.rodev.gifsapp.core.ManageResources
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideCoroutineDispatchers(): CoroutineDispatchers = CoroutineDispatchers.Main()

    @Provides
    @Singleton
    fun provideManageResources(@ApplicationContext context: Context) = ManageResources.Main(context)

}
