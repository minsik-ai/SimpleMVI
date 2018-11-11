package com.trent.simplemvi.mvi

import com.trent.simplemvi.mvi.components.MviIntent
import com.trent.simplemvi.mvi.components.MviResult
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

abstract class MviProcessorHolderImpl<I : MviIntent, R : MviResult> : MviProcessorHolder<I, R> {

    abstract fun intentProcessorLogic(intent: I): Pair<List<R>, Observable<R>?>

    final override val intentProcessor = ObservableTransformer<I, R> { intents ->
        intents.flatMap { intent: I ->
            val (sync, asyncObservable) = intentProcessorLogic(intent)
            val syncObservable = Observable.just(sync).concatMapIterable { it -> it }
            return@flatMap if (asyncObservable != null) Observable.merge(syncObservable, asyncObservable)
            else syncObservable
        }
    }
}