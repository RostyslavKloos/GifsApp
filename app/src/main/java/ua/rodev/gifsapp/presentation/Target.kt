package ua.rodev.gifsapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.receiveAsFlow
import ua.rodev.gifsapp.core.NavigationCommand
import ua.rodev.gifsapp.core.SuspendMapper

interface Target {

    interface Observe<T> {
        fun collect(owner: LifecycleOwner, collector: FlowCollector<T>)
    }

    interface SuspendUpdate<T> : SuspendMapper.Unit<T>

    interface SuspendMutable<T> : Observe<T>, SuspendUpdate<T>

    abstract class SingleUi<T>(
        private val channel: Channel<T> = Channel(Channel.UNLIMITED),
    ) : SuspendMutable<T> {
        override suspend fun map(source: T) = channel.send(source)

        override fun collect(owner: LifecycleOwner, collector: FlowCollector<T>) {
            owner.lifecycleScope.launchWhenStarted {
                channel.receiveAsFlow().collect(collector)
            }
        }
    }

    class NavigationTarget : SingleUi<NavigationCommand>()

}
