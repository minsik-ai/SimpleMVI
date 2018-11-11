package com.trent.simplemvi.mvi

import com.trent.simplemvi.mvi.components.MviResult
import com.trent.simplemvi.mvi.components.MviViewState

interface MviReducerHolder<R : MviResult, S : MviViewState> {
    fun resultReducer(prevState: S, newResult: R): S
}