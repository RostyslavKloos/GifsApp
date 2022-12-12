package ua.rodev.gifsapp.presentation.gifs

import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ua.rodev.gifsapp.R
import ua.rodev.gifsapp.core.CoroutineDispatchers
import ua.rodev.gifsapp.core.NavigationCommand
import ua.rodev.gifsapp.data.GifData
import ua.rodev.gifsapp.data.cache.DeleteGifUseCase
import ua.rodev.gifsapp.data.pagging.GifsPagingRepository
import ua.rodev.gifsapp.presentation.Target
import ua.rodev.gifsapp.presentation.gifDetail.GifDetailViewModel
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class GifsViewModel @Inject constructor(
    private val navigationTarget: Target.SuspendMutable<NavigationCommand>,
    private val handle: SavedStateHandle,
    private val repository: GifsPagingRepository,
    private val deleteGIfUseCase: DeleteGifUseCase,
    private val dispatchers: CoroutineDispatchers,
) : ViewModel() {

    val query = handle.getStateFlow(QUERY_KEY, "")
    val gifs: Flow<PagingData<GifData>> = query
        .debounce(DEBOUNCE)
        .flatMapLatest(repository::gifsFlow)
        .cachedIn(viewModelScope)

    fun onQueryChanged(value: String) {
        handle[QUERY_KEY] = value
    }

    fun goDetails(url: String) = viewModelScope.launch {
        navigationTarget.map {
            it.navigate(
                R.id.goGifDetail,
                bundleOf(
                    GifDetailViewModel.SELECTED_URL to url,
                    GifDetailViewModel.KEYWORD to query.value
                )
            )
        }
    }

    fun onDeleteGifClick(id: String, url: String) {
        viewModelScope.launch(dispatchers.io()) {
            deleteGIfUseCase.delete(id, url)
        }
    }

    companion object {
        private const val QUERY_KEY = "query"
        private const val DEBOUNCE = 300L
    }
}
