package com.trent.simplemvi

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.trent.simplemvi.mvi.components.MviIntent
import com.trent.simplemvi.mvi.components.MviResult
import com.trent.simplemvi.mvi.components.MviViewState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

abstract class MviView<I : MviIntent, R : MviResult, S : MviViewState>(private val viewModel: MviViewModel<I, R, S>) :
    LifecycleObserver {
    abstract fun intents(): Observable<I>

    abstract fun render(state: S)

    /**
     * Livedata-like
     */
    private val disposables = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Timber.d("onStart")
        disposables.add(viewModel.states().subscribe(this::render))
        // Pass the UI's intents to the ViewModel
        disposables.add(viewModel.processIntents(intents()))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Timber.d("onStop")
        disposables.clear()
    }
}