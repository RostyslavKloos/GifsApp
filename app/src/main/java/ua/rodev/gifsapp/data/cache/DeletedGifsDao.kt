package ua.rodev.gifsapp.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DeletedGifsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deletedGif: DeletedGif)

    @Query("SELECT * FROM deleted_gifs")
    suspend fun all(): List<DeletedGif>
}
