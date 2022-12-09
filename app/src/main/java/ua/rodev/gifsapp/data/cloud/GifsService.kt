package ua.rodev.gifsapp.data.cloud

import ua.rodev.gifsapp.data.GifsCloud

interface GifsService {

//    @GET("")
    suspend fun gifs(): List<GifsCloud>
}
