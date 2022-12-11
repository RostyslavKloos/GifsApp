package ua.rodev.gifsapp.data.cloud.gifs

import com.google.gson.annotations.SerializedName
import ua.rodev.gifsapp.data.GifData

data class GifsCloud(
    @SerializedName("data")
    val data : List<GifCloud>,
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("pagination")
    val pagination: Pagination
)

// TODO: use interface
fun List<GifCloud>.toGifsData(query: String): List<GifData> {
    return this.map {
        GifData(
            id = it.id,
            url = it.images.original.url,
            title = it.title,
            keyword = query
        )
    }
}
