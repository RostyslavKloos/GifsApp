package ua.rodev.gifsapp.presentation.gifs.composables

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import ua.rodev.gifsapp.R

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    url: String,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val context = LocalContext.current
    val factory = if (Build.VERSION.SDK_INT >= 28)
        ImageDecoderDecoder.Factory()
    else
        GifDecoder.Factory()
    val imageLoader = ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder(context).build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("gif_cache"))
                .build()
        }
        .respectCacheHeaders(false)
        .components {
            add(factory)
        }
        .build()

    Image(
        modifier = modifier,
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context)
                .networkCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .data(url)
                .diskCacheKey(url)
                .memoryCacheKey(url)
                .placeholder(R.drawable.ic_image_holder)
                .error(R.drawable.ic_image_error)
                .build(),
            imageLoader = imageLoader
        ),
        contentScale = contentScale,
        contentDescription = "",
    )
}
