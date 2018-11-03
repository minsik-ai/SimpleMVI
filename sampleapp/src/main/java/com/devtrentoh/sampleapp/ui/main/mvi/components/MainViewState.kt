package com.devtrentoh.sampleapp.ui.main.mvi.components

import com.trent.simplemvi.mvi.components.MviViewState

sealed class MainViewState : MviViewState {
    data class TodoListViewState(val todoList: List<TodoItem>, val selectedItem: TodoItem? = null) : MainViewState()
    sealed class TextInputViewState(open val hintText: String) : MainViewState() {
        object TodoAddViewState : TextInputViewState("Input Todo Description")
        data class TodoEditViewState(val editTarget: TodoItem, override val hintText: String): TextInputViewState(hintText)
    }
}

data class TodoItem(val uniqueIndex: Int, val description: String, val isDone: Boolean = false)