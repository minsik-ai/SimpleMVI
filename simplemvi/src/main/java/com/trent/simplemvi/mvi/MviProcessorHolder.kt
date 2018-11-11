package com.trent.simplemvi.mvi

import com.trent.simplemvi.mvi.components.MviIntent
import com.trent.simplemvi.mvi.components.MviResult
import io.reactivex.Observable

interface MviProcessorHolder<I : MviIntent, R : MviResult> {
    fun intentProcessor(intent: I): ProcessResults<R>
}

data class ProcessResults<R : MviResult>(val sync: List<R>, val async: Observable<R>? = null)