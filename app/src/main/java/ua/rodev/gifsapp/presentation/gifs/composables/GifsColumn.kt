package ua.rodev.gifsapp.presentation.gifs.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import ua.rodev.gifsapp.R
import ua.rodev.gifsapp.data.GifData
import ua.rodev.gifsapp.presentation.gifs.PagingException

@Composable
fun GifsColumn(
    listState: LazyListState,
    gifs: LazyPagingItems<GifData>,
) {
    val append = gifs.loadState.append
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = 16.dp
        )
    ) {
        items(gifs, GifData::id) { gif ->
            if (gif != null) GifCard(gif.title, gif.url)
        }
        if (gifs.loadState.refresh is LoadState.Error) {
            val errorState = gifs.loadState.refresh as LoadState.Error
            val stringRes = (errorState.error as PagingException).stringRes
            item {
                Text(
                    text = stringResource(id = stringRes),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = gifs::retry) {
                    Text(
                        text = stringResource(R.string.retry_refresh),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        if (append is LoadState.Loading) {
            item { CircularProgressIndicatorRow() }
        }
        if (append is LoadState.Error) {
            val stringRes = (append.error as PagingException).stringRes
            item {
                Text(
                    text = stringResource(id = stringRes),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = gifs::retry
                ) {
                    Text(
                        text = stringResource(R.string.retry_append),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}