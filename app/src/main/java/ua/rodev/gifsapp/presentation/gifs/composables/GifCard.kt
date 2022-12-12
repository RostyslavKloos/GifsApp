package ua.rodev.gifsapp.presentation.gifs.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.rodev.gifsapp.core.OnClick

typealias OnGifDeleteClick = (id: String, url: String) -> Unit
typealias OnCardClick = (url: String) -> Unit

@Composable
fun GifCard(
    title: String,
    url: String,
    onCardClick: OnCardClick,
    onDeleteClick: OnClick,
) = Card(
    backgroundColor = Color.LightGray,
    modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .padding(vertical = 8.dp)
        .clickable {
            onCardClick.invoke(url)
        },
    shape = RoundedCornerShape(16.dp),
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GifImage(
            modifier = Modifier
                .size(56.dp)
                .padding(horizontal = 8.dp)
                .aspectRatio(1f)
                .clip(CircleShape),
            url = url
        )
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        IconButton(onClick = onDeleteClick) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "")
        }
    }
}
