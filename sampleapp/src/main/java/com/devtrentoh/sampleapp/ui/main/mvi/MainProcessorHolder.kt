package com.devtrentoh.sampleapp.ui.main.mvi

import com.devtrentoh.sampleapp.ui.main.MainModel
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainIntent
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainResult
import com.trent.simplemvi.mvi.MviProcessorHolder
import com.trent.simplemvi.mvi.MviProcessorHolderImpl
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class MainProcessorHolder(private val model: MainModel) : MviProcessorHolderImpl<MainIntent, MainResult>() {

    private val openAddTodoIntentProcessor =
        { intent: MainIntent.OpenAddTodoIntent -> listOf(MainResult.OpenAddTodoResult) }

    private val applyAddTodoIntentProcessor =
        { intent: MainIntent.ApplyAddTodoIntent ->
            val newItem = model.addItem(intent.description)
            listOf(MainResult.SyncTodoListResult(model.getItemList(), null))
        }

    private val openEditTodoIntentProcessor =
        { intent: MainIntent.OpenEditTodoIntent ->
            listOf(MainResult.OpenEditTodoResult(intent.todoItem))
        }

    private val applyEditTodoIntentProcessor =
        { intent: MainIntent.ApplyEditTodoIntent ->
            val newItem = model.editItem(intent.editTarget, intent.newDescription)
            listOf(MainResult.SyncTodoListResult(model.getItemList(), null))
        }

    private val selectTodoIntentProcessor =
        { intent: MainIntent.SelectTodoIntent ->
            listOf(MainResult.SyncTodoListResult(model.getItemList(), intent.todoItem))
        }

    private val toggleDoneTodoIntentProcessor =
        { intent: MainIntent.ToggleDoneTodoIntent ->
            model.toggleDone(intent.todoItem)
            listOf(MainResult.SyncTodoListResult(model.getItemList(), intent.todoItem))
        }

    private val deleteTodoIntentProcessor =
        { intent: MainIntent.DeleteTodoIntent ->
            model.deleteItem(intent.todoItem)
            listOf(MainResult.SyncTodoListResult(model.getItemList(), null))
        }


    private val cancelTodoEditIntentProcessor =
        { intent: MainIntent.CancelTodoEditIntent ->
            listOf(MainResult.SyncTodoListResult(model.getItemList(), null))
        }

    override val intentProcessorLogic: (MainIntent) -> List<MainResult> = { intent ->
            when (intent) {
                is MainIntent.OpenAddTodoIntent -> openAddTodoIntentProcessor(intent)
                is MainIntent.ApplyAddTodoIntent -> applyAddTodoIntentProcessor(intent)
                is MainIntent.OpenEditTodoIntent -> openEditTodoIntentProcessor(intent)
                is MainIntent.ApplyEditTodoIntent -> applyEditTodoIntentProcessor(intent)
                is MainIntent.SelectTodoIntent -> selectTodoIntentProcessor(intent)
                is MainIntent.ToggleDoneTodoIntent -> toggleDoneTodoIntentProcessor(intent)
                is MainIntent.DeleteTodoIntent -> deleteTodoIntentProcessor(intent)
                is MainIntent.CancelTodoEditIntent -> cancelTodoEditIntentProcessor(intent)
            }
        }
}