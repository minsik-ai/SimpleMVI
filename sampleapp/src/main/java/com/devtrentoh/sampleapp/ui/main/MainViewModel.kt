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

package com.devtrentoh.sampleapp.ui.main

import com.devtrentoh.sampleapp.ui.main.mvi.components.MainIntent
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainResult
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainViewState
import com.devtrentoh.sampleapp.ui.main.mvi.components.TodoListViewState
import com.trent.simplemvi.MviViewModel
import com.trent.simplemvi.mvi.MviProcessorHolder
import com.trent.simplemvi.mvi.MviReducerHolder

class MainViewModel(
    processorHolder: MviProcessorHolder<MainIntent, MainResult>,
    reducerHolder: MviReducerHolder<MainResult, MainViewState>
) : MviViewModel<MainIntent, MainResult, MainViewState>(processorHolder, reducerHolder) {

    override val initialState: MainViewState = TodoListViewState(emptyList())
}
