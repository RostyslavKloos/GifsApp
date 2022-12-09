package ua.rodev.gifsapp.presentation.main.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rodev.gifsapp.core.NavigationCommand
import ua.rodev.gifsapp.presentation.Target
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MainActivityModule {

    @Provides
    @Singleton
    fun provideNavigationTarget(): Target.SuspendMutable<NavigationCommand> {
        return Target.NavigationTarget()
    }

    @Provides
    fun provideObserveNavigationTarget(
        target: Target.SuspendMutable<NavigationCommand>,
    ): Target.Observe<NavigationCommand> = target
}
