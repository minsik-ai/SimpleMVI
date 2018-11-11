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

package com.devtrentoh.sampleapp.ui.main.mvi.components

import com.trent.simplemvi.mvi.components.MviIntent

sealed class MainIntent : MviIntent

object OpenAddTodoIntent : MainIntent()
data class ApplyAddTodoIntent(val description: String) : MainIntent()

data class OpenEditTodoIntent(val todoItem: TodoItem) : MainIntent()
data class ApplyEditTodoIntent(val editTarget: TodoItem, val newDescription: String) : MainIntent()

data class SelectTodoIntent(val todoItem: TodoItem) : MainIntent()
data class ToggleDoneTodoIntent(val todoItem: TodoItem) : MainIntent()

data class DeleteTodoIntent(val todoItem: TodoItem) : MainIntent()
object CancelTodoEditIntent : MainIntent()