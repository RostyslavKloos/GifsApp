package ua.rodev.gifsapp.presentation.gifs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.style.TextAlign
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint
import ua.rodev.gifsapp.data.GifData
import ua.rodev.gifsapp.presentation.gifs.composables.GifsColumn

@AndroidEntryPoint
class GifsFragment : Fragment() {

    private val viewModel by viewModels<GifsViewModel>()

    @OptIn(ExperimentalLifecycleComposeApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {

            val query by viewModel.query.collectAsStateWithLifecycle()
            val gifs: LazyPagingItems<GifData> = viewModel.gifs.collectAsLazyPagingItems()
            val isRefreshing by remember { derivedStateOf { gifs.loadState.refresh is LoadState.Loading } }
            val isErrorOnFirstPage by remember { derivedStateOf { gifs.loadState.refresh is LoadState.Error } }
            val isError by remember { derivedStateOf { gifs.loadState.append is LoadState.Error } }

            SideEffect {
                if (isErrorOnFirstPage) {
                    val errorState = gifs.loadState.refresh as LoadState.Error
                    Log.e("RORO", "isErrorOnFirstPage ${errorState.error}")
                    return@SideEffect // Just to prevent 2x toasts
                }
                if (isError) {
                    val errorState = gifs.loadState.append as LoadState.Error
                    Log.e("RORO", "ERROR ${errorState.error}")
                }
//                val imageLoader = context.imageLoader
//                imageLoader.memoryCache?.remove(MemoryCache.Key("https://media0.giphy.com/media/ICOgUNjpvO0PC/giphy.gif?cid=915a99aazrdjigdtxazlaxofhvaypi46ydmjiti5ru66fw8b&rid=giphy.gif&ct=g"))
//                imageLoader.diskCache?.remove("https://media0.giphy.com/media/ICOgUNjpvO0PC/giphy.gif?cid=915a99aazrdjigdtxazlaxofhvaypi46ydmjiti5ru66fw8b&rid=giphy.gif&ct=g")
//
            }

            Column(modifier = Modifier.fillMaxSize()) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = query,
                    onValueChange = viewModel::onQueryChanged
                )
                if (gifs.itemCount == 0 && gifs.loadState.refresh !is LoadState.Loading) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "No results",
                            textAlign = TextAlign.Center
                        )
                    }
                }
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = gifs::refresh,
                    indicator = { state, trigger ->
                        SwipeRefreshIndicator(
                            state = state,
                            refreshTriggerDistance = trigger,
                            scale = true,
                            backgroundColor = DarkGray,
                            contentColor = White
                        )
                    }
                ) {
                    GifsColumn(
                        listState = rememberLazyListState(),
                        gifs = gifs
                    )
                }
            }
        }
    }
}
