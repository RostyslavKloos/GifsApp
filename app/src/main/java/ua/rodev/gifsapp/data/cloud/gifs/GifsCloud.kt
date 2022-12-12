package ua.rodev.gifsapp.data.cloud.gifs

import com.google.gson.annotations.SerializedName
import ua.rodev.gifsapp.data.cache.DeletedGifsDao

data class GifsCloud(
    @SerializedName("data")
    private val data: List<GifCloud>,
    @SerializedName("meta")
    private val meta: Meta,
    @SerializedName("pagination")
    private val pagination: Pagination,
) {
    interface Mapper<T> {
        suspend fun map(data: List<GifCloud>): T

        class WithoutDeletedGifsMapper(
            private val deletedGifsDao: DeletedGifsDao,
        ) : Mapper<List<GifCloud>> {
            override suspend fun map(data: List<GifCloud>): List<GifCloud> {
                val deletedGifs = deletedGifsDao.all()
                return data.filter { gifCloud ->
                    deletedGifs.firstOrNull {
                        gifCloud.map(GifCloud.Mapper.Matches(it.id))
                    } == null
                }
            }
        }
    }

    suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(data)

    fun isSuccessful(): Boolean = meta.status == SUCCESS_STATUS

    companion object {
        private const val SUCCESS_STATUS = 200
    }
}
