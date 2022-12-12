package ua.rodev.gifsapp.presentation.gifDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import dagger.hilt.android.AndroidEntryPoint
import ua.rodev.gifsapp.presentation.gifs.composables.GifImage
import ua.rodev.gifsapp.presentation.theme.GifTheme

@AndroidEntryPoint
class GifDetailFragment : Fragment() {

    private val viewModel by viewModels<GifDetailViewModel>()

    @OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalPagerApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {

            val gifs by viewModel.gifs.collectAsStateWithLifecycle()
            val pagerState = rememberPagerState(gifs.size)
            LaunchedEffect(pagerState, gifs) {
                if (gifs.isNotEmpty()) {
                    val current = gifs.first { it.url == viewModel.url }
                    pagerState.scrollToPage(gifs.indexOf(current))
                }
            }
            GifTheme {
                Column(Modifier.fillMaxSize()) {
                    TopAppBar(
                        title = {
                            Text(text = "Gifs app")
                        },
                        navigationIcon = {
                            IconButton(onClick = viewModel::goBack) {
                                Icon(Icons.Filled.ArrowBack, "")
                            }
                        },
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = Color.White,
                        elevation = 10.dp
                    )
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxSize()
                            .systemBarsPadding(),
                        state = pagerState,
                        count = gifs.size
                    ) {
                        GifImage(
                            modifier = Modifier.fillMaxSize(),
                            url = gifs[it].url,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}
