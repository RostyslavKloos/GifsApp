package ua.rodev.gifsapp.data.cloud

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ua.rodev.gifsapp.data.cloud.gifs.GifsCloud

interface GifsService {

    // TODO remove api_key
    @GET("gifs/search?")
    suspend fun gifs(
        @Query("api_key") apiKey: String = "YGHnKKBGSydS6nSt6WAoUcICWwmgCfvL",
        @Query("q") query: String = "cat",
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
    ): Response<GifsCloud>
}
