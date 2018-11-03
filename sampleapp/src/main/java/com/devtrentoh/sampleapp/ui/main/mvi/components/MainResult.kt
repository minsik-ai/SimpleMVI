package com.devtrentoh.sampleapp.ui.main.mvi.components

import com.trent.simplemvi.mvi.components.MviResult

sealed class MainResult : MviResult {
    object OpenAddTodoResult : MainResult()

    data class OpenEditTodoResult(val targetItem: TodoItem) : MainResult()

    data class SyncTodoListResult(
        val items: List<TodoItem>,
        val newSelectedItem: TodoItem?
    ) : MainResult()
}