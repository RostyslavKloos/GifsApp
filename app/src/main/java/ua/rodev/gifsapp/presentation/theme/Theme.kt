package ua.rodev.gifsapp.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun GifTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    val view = LocalView.current
    val primaryColor = MaterialTheme.colors.primary
    if (!view.isInEditMode) {
        SideEffect {
            if (darkTheme) {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent
                )
            } else {
                systemUiController.setSystemBarsColor(
                    color = primaryColor
                )
            }
        }
    }

    MaterialTheme(
        typography = MaterialTheme.typography,
        content = content,
    )
}
