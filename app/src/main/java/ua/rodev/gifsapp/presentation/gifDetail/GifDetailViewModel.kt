package ua.rodev.gifsapp.presentation.gifDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ua.rodev.gifsapp.core.CoroutineDispatchers
import ua.rodev.gifsapp.core.NavigationCommand
import ua.rodev.gifsapp.data.GifData
import ua.rodev.gifsapp.data.cache.GifsDao
import ua.rodev.gifsapp.presentation.Target
import javax.inject.Inject

@HiltViewModel
class GifDetailViewModel @Inject constructor(
    handle: SavedStateHandle,
    dispatchers: CoroutineDispatchers,
    private val dao: GifsDao,
    private val navigationTarget: Target.SuspendMutable<NavigationCommand>,
) : ViewModel() {

    private val keyword = handle.get<String>(KEYWORD) ?: ""
    val url = handle.get<String>(SELECTED_URL) ?: ""
    val gifs = MutableStateFlow<List<GifData>>(emptyList())

    init {
        viewModelScope.launch(dispatchers.io()) {
            gifs.value = dao.gifsByKeyword(keyword)
        }
    }

    fun goBack() {
        viewModelScope.launch {
            navigationTarget.map {
                it.navigateUp()
            }
        }
    }

    companion object {
        const val SELECTED_URL = "selectedUrl"
        const val KEYWORD = "keyword"
    }
}
