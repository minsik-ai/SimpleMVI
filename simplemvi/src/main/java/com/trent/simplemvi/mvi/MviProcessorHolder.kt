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

package com.trent.simplemvi.mvi

import com.trent.simplemvi.mvi.components.MviIntent
import com.trent.simplemvi.mvi.components.MviResult
import io.reactivex.Observable

interface MviProcessorHolder<I : MviIntent, R : MviResult> {
    fun intentProcessor(intent: I): ProcessResults<R>
}

data class ProcessResults<out R : MviResult>(val sync: List<R>, val async: Observable<out R>? = null)