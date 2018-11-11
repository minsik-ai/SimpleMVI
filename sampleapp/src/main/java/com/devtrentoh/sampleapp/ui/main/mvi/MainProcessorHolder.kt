package com.devtrentoh.sampleapp.ui.main.mvi

import com.devtrentoh.sampleapp.ui.main.MainModel
import com.devtrentoh.sampleapp.ui.main.mvi.components.*
import com.trent.simplemvi.mvi.MviProcessorHolder
import com.trent.simplemvi.mvi.ProcessResults

class MainProcessorHolder(private val model: MainModel) : MviProcessorHolder<MainIntent, MainResult> {

    override fun intentProcessor(intent: MainIntent): ProcessResults<MainResult> {
        val syncResults = when (intent) {
            is OpenAddTodoIntent -> listOf(OpenAddTodoResult)
            is ApplyAddTodoIntent -> {
                model.addItem(intent.description)
                listOf(SyncTodoListResult(model.getItemList(), null))
            }
            is OpenEditTodoIntent -> listOf(OpenEditTodoResult(intent.todoItem))
            is ApplyEditTodoIntent -> {
                model.editItem(intent.editTarget, intent.newDescription)
                listOf(SyncTodoListResult(model.getItemList(), null))
            }
            is SelectTodoIntent -> listOf(
                SyncTodoListResult(model.getItemList(), intent.todoItem)
            )
            is ToggleDoneTodoIntent -> {
                model.toggleDone(intent.todoItem)
                listOf(SyncTodoListResult(model.getItemList(), intent.todoItem))
            }
            is DeleteTodoIntent -> {
                model.deleteItem(intent.todoItem)
                listOf(SyncTodoListResult(model.getItemList(), null))
            }
            is CancelTodoEditIntent -> listOf(SyncTodoListResult(model.getItemList(), null))
        }

        return ProcessResults(syncResults)
    }
}