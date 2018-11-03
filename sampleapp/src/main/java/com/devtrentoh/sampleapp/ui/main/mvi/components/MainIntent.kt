package com.devtrentoh.sampleapp.ui.main.mvi.components

import com.trent.simplemvi.mvi.components.MviIntent

sealed class MainIntent : MviIntent {
    object OpenAddTodoIntent : MainIntent()
    data class ApplyAddTodoIntent(val description: String) : MainIntent()

    data class CheckDoneTodoIntent(val todoItem: TodoItem) : MainIntent()

    data class OpenEditTodoIntent(val todoItem: TodoItem) : MainIntent()
    data class ApplyEditTodoIntent(val newDescription: String) : MainIntent()

    data class ToggleSelectTodoIntent(val todoItem: TodoItem): MainIntent()

//    object CancelModifyTodoListIntent : MainIntent()

    // TODO : DeleteToDoIntent
}