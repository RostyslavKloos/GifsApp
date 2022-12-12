package ua.rodev.gifsapp.data.cloud.gifs

import com.google.gson.annotations.SerializedName
import ua.rodev.gifsapp.data.GifData
import ua.rodev.gifsapp.data.cache.GifsRemoteKeys

data class GifCloud(
    @SerializedName("id")
    private val id: String,
    @SerializedName("images")
    private val images: Images,
    @SerializedName("title")
    private val title: String,
) {
    interface Mapper<T> {
        fun map(id: String, url: String, title: String): T

        class Matches(private val id: String) : Mapper<Boolean> {
            override fun map(id: String, url: String, title: String): Boolean = this.id == id
        }

        class GifCloudToGifDataMapper(
            private val keyword: String,
        ) : Mapper<GifData> {
            override fun map(id: String, url: String, title: String): GifData {
                return GifData(
                    id = id,
                    url = url,
                    title = title,
                    keyword = keyword
                )
            }
        }
    }

    fun toRemoteKey(prevKey: Int?, nextKey: Int?): GifsRemoteKeys {
        return GifsRemoteKeys(id, prevKey, nextKey)
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(id, images.original.url, title)
}
