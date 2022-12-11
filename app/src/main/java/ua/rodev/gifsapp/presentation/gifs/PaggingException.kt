package ua.rodev.gifsapp.presentation.gifs

import ua.rodev.gifsapp.R

sealed class PagingException(open val stringRes: Int) : Exception() {
    object NetworkError : PagingException(R.string.network_error)
    class Error(stringRes: Int = R.string.network_error) : PagingException(stringRes)
}
