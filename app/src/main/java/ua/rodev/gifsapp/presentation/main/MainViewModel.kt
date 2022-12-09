package ua.rodev.gifsapp.presentation.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.FlowCollector
import ua.rodev.gifsapp.core.NavigationCommand
import ua.rodev.gifsapp.presentation.Target
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val navigationTarget: Target.Observe<NavigationCommand>,
) : ViewModel(), Target.Observe<NavigationCommand> {

    override fun collect(owner: LifecycleOwner, collector: FlowCollector<NavigationCommand>) =
        navigationTarget.collect(owner, collector)
}
