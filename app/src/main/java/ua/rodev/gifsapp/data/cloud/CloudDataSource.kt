package ua.rodev.gifsapp.data.cloud

import ua.rodev.gifsapp.data.FetchGifs
import ua.rodev.gifsapp.data.GifsData

interface CloudDataSource : FetchGifs {

    class Main(private val service: GifsService) : CloudDataSource {
        override fun gifs(): GifsData {
            return service.gifs().map(mapper)
        }
    }
}