package com.trent.simplemvi.mvi

import com.trent.simplemvi.mvi.components.MviResult
import com.trent.simplemvi.mvi.components.MviViewState
import io.reactivex.functions.BiFunction

interface MviReducerHolder<R : MviResult, S : MviViewState> {
    val resultReducer: Function2<S, R, S>
}