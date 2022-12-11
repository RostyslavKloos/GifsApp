package ua.rodev.gifsapp.presentation.gifs

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ua.rodev.gifsapp.R
import ua.rodev.gifsapp.core.NavigationCommand
import ua.rodev.gifsapp.data.GifData
import ua.rodev.gifsapp.data.pagging.GifsPagingRepository
import ua.rodev.gifsapp.presentation.Target
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class GifsViewModel @Inject constructor(
    private val navigationTarget: Target.SuspendMutable<NavigationCommand>,
    private val handle: SavedStateHandle,
    private val repository: GifsPagingRepository
) : ViewModel() {

    val query = handle.getStateFlow(QUERY_KEY, "")
    val gifs: Flow<PagingData<GifData>> = query
        .flatMapLatest(repository::gifsFlow)
        .cachedIn(viewModelScope)

    fun onQueryChanged(value: String) {
        handle[QUERY_KEY] = value
    }

    fun goDetails() = viewModelScope.launch {
        navigationTarget.map {
            it.navigate(R.id.goGifDetail)
        }
    }

    companion object {
        private const val QUERY_KEY = "query"
    }
}
