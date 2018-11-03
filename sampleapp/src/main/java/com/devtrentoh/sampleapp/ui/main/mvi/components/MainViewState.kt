package com.devtrentoh.sampleapp.ui.main.mvi.components

import com.trent.simplemvi.mvi.components.MviViewState

sealed class MainViewState : MviViewState {
    data class TodoListViewState(val todoList: List<TodoItem>) : MviViewState
    sealed class TextInputViewState(open val hintText: String) : MviViewState {
        data class TodoAddViewState(override val hintText: String) : TextInputViewState(hintText)
        data class TodoEditViewState(val editTarget: TodoItem, override val hintText: String): TextInputViewState(hintText)
    }
}

data class TodoItem(val uniqueIndex: Int, val description: String, val isDone: Boolean = false)