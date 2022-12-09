package ua.rodev.gifsapp.core

interface SuspendMapper<S, R> {

    suspend fun map(source: S): R

    interface Unit<T> : SuspendMapper<T, kotlin.Unit>
}
