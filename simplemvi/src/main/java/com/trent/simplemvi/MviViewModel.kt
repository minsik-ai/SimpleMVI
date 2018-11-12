/*
 *    Copyright 2018 Trent Oh
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.trent.simplemvi

import androidx.lifecycle.ViewModel
import com.trent.simplemvi.mvi.MviProcessorHolder
import com.trent.simplemvi.mvi.MviReducerHolder
import com.trent.simplemvi.mvi.components.MviIntent
import com.trent.simplemvi.mvi.components.MviResult
import com.trent.simplemvi.mvi.components.MviViewState
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

abstract class MviViewModel<I : MviIntent, R : MviResult, S : MviViewState>(
    private val processorHolder: MviProcessorHolder<I, R>,
    private val reducerHolder: MviReducerHolder<R, S>
) : ViewModel() {

    private val intentsSubject: PublishSubject<I> = PublishSubject.create()

    abstract val initialState: S

    private var cachedState: S? = null

    fun processIntents(intents: Observable<I>): Disposable {
        return intents.subscribe { intentsSubject.onNext(it) }
    }

    fun states(): Observable<S> {
        return intentsSubject
            .compose(this::processToObservable)
            .scan(cachedState ?: initialState, reducerHolder::resultReducer)
            .doOnNext { cachedState = it }
    }

    private fun processToObservable(intents: Observable<I>) = intents.flatMap { intent: I ->
        val (sync, asyncObservable) = processorHolder.intentProcessor(intent)
        val syncObservable =
            if (sync.isNotEmpty()) Observable.just(sync).concatMapIterable { it -> it } else Observable.empty()

        if (asyncObservable != null) Observable.merge(syncObservable, asyncObservable)
        else syncObservable
    }
}