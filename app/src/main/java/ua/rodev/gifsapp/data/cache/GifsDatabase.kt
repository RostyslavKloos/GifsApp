package ua.rodev.gifsapp.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.rodev.gifsapp.data.GifData

@Database(
    version = 1,
    entities = [
        GifData::class,
        GifsRemoteKeys::class,
    ],
)
abstract class GifsDatabase : RoomDatabase() {
    abstract fun gifsDao(): GifsDao
    abstract fun gifsRemoteKeysDao(): GifsRemoteKeysDao
}
