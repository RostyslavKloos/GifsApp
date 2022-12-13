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
    onCardClick: OnCardClick,
    onDeleteClick: OnGifDeleteClick,
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = 16.dp
        )
    ) {
        items(gifs, GifData::id) { gif ->
            if (gif != null)
                GifCard(
                    title = gif.title,
                    url = gif.url,
                    onCardClick = { onCardClick.invoke(gif.url) },
                    onDeleteClick = { onDeleteClick.invoke(gif.id, gif.url) },
                )
        }
        val refreshLoadState = gifs.loadState.refresh
        if (refreshLoadState is LoadState.Error) {
            val stringRes = (refreshLoadState.error as PagingException).stringRes
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
                        text = stringResource(R.string.retry_refresh),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        val appendLoadState = gifs.loadState.append
        if (appendLoadState is LoadState.Loading) {
            item { CircularProgressIndicatorRow() }
        }
        if (appendLoadState is LoadState.Error) {
            val stringRes = (appendLoadState.error as PagingException).stringRes
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
