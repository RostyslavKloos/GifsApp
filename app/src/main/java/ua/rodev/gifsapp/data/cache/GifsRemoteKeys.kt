package ua.rodev.gifsapp.data.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gifs_remote_keys")
data class GifsRemoteKeys(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
