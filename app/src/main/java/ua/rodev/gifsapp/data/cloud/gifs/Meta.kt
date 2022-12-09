package ua.rodev.gifsapp.data.cloud.gifs

import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("msg")
    val message: String,
    @SerializedName("response_id")
    val responseId: String,
    @SerializedName("status")
    val status: Int
)
