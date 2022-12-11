package ua.rodev.gifsapp.data.cloud

import ua.rodev.gifsapp.data.FetchGifs
import ua.rodev.gifsapp.data.GifData

interface CloudDataSource : FetchGifs {

    class Main(private val service: GifsService) : CloudDataSource {
        override fun gifs(): GifData {
//            return service.gifs().map(mapper)
            return GifData("","","", "")
        }
    }
}
