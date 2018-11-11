package com.trent.simplemvi.mvi

import com.trent.simplemvi.mvi.components.MviIntent
import com.trent.simplemvi.mvi.components.MviResult
import io.reactivex.Observable

interface MviProcessorHolder<I : MviIntent, R : MviResult> {
    fun intentProcessor(intent: I): Pair<List<R>, Observable<R>?>
}