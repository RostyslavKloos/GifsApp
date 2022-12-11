package ua.rodev.gifsapp.data.cloud.gifs

import com.google.gson.annotations.SerializedName

data class GifCloud(
    @SerializedName("id")
    val id: String,
    @SerializedName("images")
    val images: Images,
    @SerializedName("title")
    val title: String,
)
