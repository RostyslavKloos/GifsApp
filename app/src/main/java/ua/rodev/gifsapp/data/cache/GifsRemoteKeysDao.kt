package ua.rodev.gifsapp.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GifsRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<GifsRemoteKeys>)

    @Query("SELECT * FROM gifs_remote_keys WHERE id= :id")
    suspend fun remoteKeysGifId(id: String): GifsRemoteKeys?

    @Query("DELETE FROM gifs_remote_keys")
    suspend fun deleteRemoteKeys()
}
