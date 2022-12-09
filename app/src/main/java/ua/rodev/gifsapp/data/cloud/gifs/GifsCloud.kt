package ua.rodev.gifsapp.data.cloud.gifs

import com.google.gson.annotations.SerializedName

data class GifsCloud(
    @SerializedName("data")
    val data : List<GifCloud>,
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("pagination")
    val pagination: Pagination
)
