package com.devtrentoh.sampleapp.ui.main.mvi.components

import com.trent.simplemvi.mvi.components.MviViewState

sealed class MainViewState : MviViewState {
    data class TodoListViewState(val todoList: List<TodoItem>) : MviViewState
    sealed class TextInputViewState(open val currentText: String) : MviViewState {
        data class TodoAddViewState(override val currentText: String) : TextInputViewState(currentText)
        data class TodoEditViewState(val editTarget: TodoItem, override val currentText: String): TextInputViewState(currentText)
    }
}

data class TodoItem(val uniqueIndex: Int, val description: String, val isDone: Boolean = false)