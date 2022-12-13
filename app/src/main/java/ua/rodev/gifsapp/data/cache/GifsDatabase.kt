package ua.rodev.gifsapp.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.rodev.gifsapp.data.GifData

@Database(
    version = 2,
    entities = [
        GifData::class,
        GifsRemoteKeys::class,
        DeletedGif::class
    ],
    exportSchema = false
)
abstract class GifsDatabase : RoomDatabase() {
    abstract fun gifsDao(): GifsDao
    abstract fun gifsRemoteKeysDao(): GifsRemoteKeysDao
    abstract fun deletedGifsDao(): DeletedGifsDao
}
