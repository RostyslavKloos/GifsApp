package ua.rodev.gifsapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gifs_table")
data class GifData(
    @PrimaryKey val id: String,
    val url: String,
    val title: String,
    val keyword: String
)
