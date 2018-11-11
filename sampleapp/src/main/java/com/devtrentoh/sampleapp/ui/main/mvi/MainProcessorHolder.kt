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