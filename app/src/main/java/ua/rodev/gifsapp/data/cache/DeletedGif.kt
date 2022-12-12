package ua.rodev.gifsapp.data.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_gifs")
data class DeletedGif(@PrimaryKey val id: String)
