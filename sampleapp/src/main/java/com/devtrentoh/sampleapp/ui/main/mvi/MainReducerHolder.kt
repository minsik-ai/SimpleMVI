package com.devtrentoh.sampleapp.ui.main.mvi

import com.devtrentoh.sampleapp.ui.main.mvi.components.*
import com.trent.simplemvi.mvi.MviReducerHolder
import io.reactivex.functions.BiFunction

class MainReducerHolder : MviReducerHolder<MainResult, MainViewState> {
    override val resultReducer = BiFunction<MainViewState, MainResult, MainViewState> { prevState, newResult ->
        when (prevState) {
            is TodoListViewState ->
                when (newResult) {
                    OpenAddTodoResult -> TodoAddViewState
                    is OpenEditTodoResult -> TodoEditViewState(newResult.targetItem)
                    is SyncTodoListResult -> prevState.copy(
                        todoList = newResult.items,
                        selectedItem = newResult.newSelectedItem
                    )
                }
            TodoAddViewState ->
                when (newResult) {
                    OpenAddTodoResult -> prevState // possible error
                    is OpenEditTodoResult -> prevState // possible error
                    is SyncTodoListResult -> TodoListViewState(newResult.items, null)
                }
            is TodoEditViewState ->
                when (newResult) {
                    OpenAddTodoResult -> prevState // possible error
                    is OpenEditTodoResult -> prevState // possible error
                    is SyncTodoListResult -> TodoListViewState(newResult.items, null)
                }
        }
    }

}