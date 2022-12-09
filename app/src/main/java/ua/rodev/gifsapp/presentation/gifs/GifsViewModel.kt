package ua.rodev.gifsapp.presentation.gifs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.rodev.gifsapp.R
import ua.rodev.gifsapp.core.NavigationCommand
import ua.rodev.gifsapp.presentation.Target
import javax.inject.Inject

@HiltViewModel
class GifsViewModel @Inject constructor(
    private val navigationTarget: Target.SuspendMutable<NavigationCommand>,
) : ViewModel() {

    fun goDetails() = viewModelScope.launch {
        navigationTarget.map {
            it.navigate(R.id.goGifDetail)
        }
    }
}
