/*
 *    Copyright 2018 Trent Oh
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.devtrentoh.sampleapp.ui.main.mvi

import com.devtrentoh.sampleapp.ui.main.mvi.components.*
import com.trent.simplemvi.mvi.MviReducerHolder

class MainReducerHolder : MviReducerHolder<MainResult, MainViewState> {

    override fun resultReducer(prevState: MainViewState, newResult: MainResult): MainViewState =
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