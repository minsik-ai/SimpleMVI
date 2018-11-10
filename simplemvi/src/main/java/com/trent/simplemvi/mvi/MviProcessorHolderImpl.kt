package com.trent.simplemvi.mvi

import com.trent.simplemvi.mvi.components.MviIntent
import com.trent.simplemvi.mvi.components.MviResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class MviProcessorHolderImpl<I : MviIntent, R : MviResult> : MviProcessorHolder<I, R> {

    abstract val intentProcessorLogic: Function1<I, List<R>>

    final override val intentProcessor = ObservableTransformer<I, R> { intents ->
        intents.flatMap { intent: I -> Observable.just(intentProcessorLogic(intent)).concatMapIterable { it -> it } }
    }
}