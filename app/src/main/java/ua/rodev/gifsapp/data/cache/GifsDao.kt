package ua.rodev.gifsapp.data.cache

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ua.rodev.gifsapp.data.GifData

@Dao
interface GifsDao {

    @Transaction
    suspend fun deleteAndInsertGifs(gifs: List<GifData>) {
        deleteGifs()
        insertGifs(gifs)
    }

    @Query("SELECT * FROM gifs_table WHERE keyword LIKE :query")
    fun gifsPagingSourceByQuery(query: String): PagingSource<Int, GifData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGifs(gifs: List<GifData>)

    @Query("SELECT * from gifs_table")
    fun gifsFlow(): Flow<List<GifData>>

    @Query("DELETE from gifs_table")
    suspend fun deleteGifs()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(gifs: List<GifData>)

    @Query("DELETE FROM gifs_table")
    suspend fun clearAll()
}
