package com.devtrentoh.sampleapp.ui.main.mvi

import com.devtrentoh.sampleapp.ui.main.mvi.components.MainResult
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainViewState
import com.trent.simplemvi.mvi.MviReducerHolder
import io.reactivex.functions.BiFunction

class MainReducerHolder : MviReducerHolder<MainResult, MainViewState> {
    override val resultReducer = BiFunction<MainViewState, MainResult, MainViewState> { prevState, newResult ->
        when (prevState) {
            is MainViewState.TodoListViewState ->
                when (newResult) {
                    MainResult.OpenAddTodoResult -> MainViewState.TextInputViewState.TodoAddViewState
                    is MainResult.OpenEditTodoResult -> MainViewState.TextInputViewState.TodoEditViewState(newResult.targetItem)
                    is MainResult.SyncTodoListResult -> prevState.copy(
                        todoList = newResult.items,
                        selectedItem = if (newResult.selectedItemChanged) newResult.newSelectedItem else prevState.selectedItem
                    )
                }
            MainViewState.TextInputViewState.TodoAddViewState ->
                when (newResult) {
                    MainResult.OpenAddTodoResult -> prevState // possible error
                    is MainResult.OpenEditTodoResult -> prevState // possible error
                    is MainResult.SyncTodoListResult -> MainViewState.TodoListViewState(
                        newResult.items,
                        null
                    )
                }
            is MainViewState.TextInputViewState.TodoEditViewState ->
                when (newResult) {
                    MainResult.OpenAddTodoResult -> prevState // possible error
                    is MainResult.OpenEditTodoResult -> prevState // possible error
                    is MainResult.SyncTodoListResult -> MainViewState.TodoListViewState(
                        newResult.items,
                        null
                    )
                }
        }
    }

}