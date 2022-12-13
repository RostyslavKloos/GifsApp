package ua.rodev.gifsapp.presentation.gifs

import ua.rodev.gifsapp.R

sealed class PagingException(val stringRes: Int) : Throwable() {
    object NetworkError : PagingException(R.string.network_error)
    class Error : PagingException(R.string.network_error)
}
