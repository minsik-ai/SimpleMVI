package com.devtrentoh.sampleapp.ui.main.mvi

import com.devtrentoh.sampleapp.ui.main.MainModel
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainIntent
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainResult
import com.trent.simplemvi.mvi.MviProcessorHolder
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class MainProcessorHolder(private val model: MainModel) : MviProcessorHolder<MainIntent, MainResult> {

    private val openAddTodoIntentProcessor =
        ObservableTransformer<MainIntent.OpenAddTodoIntent, MainResult.OpenAddTodoResult> { intents ->
            intents.flatMap { _ -> Observable.just(MainResult.OpenAddTodoResult) }
        }

    private val applyAddTodoIntentProcessor =
        ObservableTransformer<MainIntent.ApplyAddTodoIntent, MainResult.SyncTodoListResult> { intents ->
            intents.flatMap { intent ->
                val newItem = model.addItem(intent.description)
                Observable.just(MainResult.SyncTodoListResult(model.getItemList(), true, newItem))
            }

        }

    private val openEditTodoIntentProcessor =
        ObservableTransformer<MainIntent.OpenEditTodoIntent, MainResult.OpenEditTodoResult> { intents ->
            intents.flatMap { intent ->
                Observable.just(MainResult.OpenEditTodoResult(intent.todoItem))
            }
        }

    private val applyEditTodoIntentProcessor =
        ObservableTransformer<MainIntent.ApplyEditTodoIntent, MainResult.SyncTodoListResult> { intents ->
            intents.flatMap { intent ->
                val newItem = model.editItem(intent.editTarget, intent.newDescription)
                Observable.just(MainResult.SyncTodoListResult(model.getItemList(), true, newItem))
            }
        }

    private val selectTodoIntentProcessor =
        ObservableTransformer<MainIntent.SelectTodoIntent, MainResult.SyncTodoListResult> { intents ->
            intents.flatMap { intent ->
                Observable.just(MainResult.SyncTodoListResult(model.getItemList(), true, intent.todoItem))
            }
        }

    private val toggleDoneTodoIntentProcessor =
        ObservableTransformer<MainIntent.ToggleDoneTodoIntent, MainResult.SyncTodoListResult> { intents ->
            intents.flatMap { intent ->
                model.toggleDone(intent.todoItem)
                Observable.just(MainResult.SyncTodoListResult(model.getItemList(), false))
            }
        }

    override val intentProcessor = ObservableTransformer<MainIntent, MainResult> { intents ->
        intents.publish { shared ->
            Observable.merge(
                listOf(
                    shared.ofType(MainIntent.OpenAddTodoIntent::class.java).compose(openAddTodoIntentProcessor),
                    shared.ofType(MainIntent.ApplyAddTodoIntent::class.java).compose(applyAddTodoIntentProcessor),
                    shared.ofType(MainIntent.OpenEditTodoIntent::class.java).compose(openEditTodoIntentProcessor),
                    shared.ofType(MainIntent.ApplyEditTodoIntent::class.java).compose(applyEditTodoIntentProcessor),
                    shared.ofType(MainIntent.SelectTodoIntent::class.java).compose(selectTodoIntentProcessor),
                    shared.ofType(MainIntent.ToggleDoneTodoIntent::class.java).compose(toggleDoneTodoIntentProcessor)
                )
            )
        }
    }

}