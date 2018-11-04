package com.trent.simplemvi.mvi

import com.trent.simplemvi.mvi.components.MviIntent
import com.trent.simplemvi.mvi.components.MviResult
import io.reactivex.ObservableTransformer

interface MviProcessorHolder<I : MviIntent, R : MviResult> {
    val intentProcessor: ObservableTransformer<I, R>
}