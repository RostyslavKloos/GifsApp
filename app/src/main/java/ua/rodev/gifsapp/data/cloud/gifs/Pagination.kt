package ua.rodev.gifsapp.data.cloud.gifs

import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("count")
    val count: Int,
    @SerializedName("offset")
    val offset: Int,
)
