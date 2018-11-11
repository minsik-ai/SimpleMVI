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
