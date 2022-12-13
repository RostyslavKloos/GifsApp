package ua.rodev.gifsapp.data.cloud

import retrofit2.http.GET
import retrofit2.http.Query
import ua.rodev.gifsapp.BuildConfig
import ua.rodev.gifsapp.data.cloud.gifs.GifsCloud

interface GifsService {

    // TODO remove api_key
    @GET("gifs/search?")
    suspend fun gifs(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("q") query: String = "cat",
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): GifsCloud
}
