package com.devtrentoh.sampleapp.ui.main.mvi

import com.devtrentoh.sampleapp.ui.main.MainModel
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainIntent
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainResult
import com.trent.simplemvi.mvi.MviProcessorHolder
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class MainProcessorHolder(private val model: MainModel) : MviProcessorHolder<MainIntent, MainResult> {

    private val openAddTodoIntentProcessor =
        { intent: MainIntent.OpenAddTodoIntent -> Observable.just(MainResult.OpenAddTodoResult) }

    private val applyAddTodoIntentProcessor =
        { intent: MainIntent.ApplyAddTodoIntent ->
            val newItem = model.addItem(intent.description)
            Observable.just(MainResult.SyncTodoListResult(model.getItemList(), null))
        }

    private val openEditTodoIntentProcessor =
        { intent: MainIntent.OpenEditTodoIntent ->
            Observable.just(MainResult.OpenEditTodoResult(intent.todoItem))
        }

    private val applyEditTodoIntentProcessor =
        { intent: MainIntent.ApplyEditTodoIntent ->
            val newItem = model.editItem(intent.editTarget, intent.newDescription)
            Observable.just(MainResult.SyncTodoListResult(model.getItemList(), null))
        }

    private val selectTodoIntentProcessor =
        { intent: MainIntent.SelectTodoIntent ->
            Observable.just(MainResult.SyncTodoListResult(model.getItemList(), intent.todoItem))
        }

    private val toggleDoneTodoIntentProcessor =
        { intent: MainIntent.ToggleDoneTodoIntent ->
            model.toggleDone(intent.todoItem)
            Observable.just(MainResult.SyncTodoListResult(model.getItemList(), intent.todoItem))
        }

    private val deleteTodoIntentProcessor =
        { intent: MainIntent.DeleteTodoIntent ->
            model.deleteItem(intent.todoItem)
            Observable.just(MainResult.SyncTodoListResult(model.getItemList(), null))
        }


    private val cancelTodoEditIntentProcessor =
        { intent: MainIntent.CancelTodoEditIntent ->
            Observable.just(MainResult.SyncTodoListResult(model.getItemList(), null))
        }

    override val intentProcessor = ObservableTransformer<MainIntent, MainResult> { intents ->
        intents.flatMap { intent: MainIntent ->
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

}