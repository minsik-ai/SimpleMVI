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

import com.trent.simplemvi.mvi.components.MviViewState

sealed class MainViewState : MviViewState

data class TodoListViewState(val todoList: List<TodoItem>, val selectedItem: TodoItem? = null) : MainViewState()

sealed class TextInputViewState(open val hintText: String) : MainViewState()
object TodoAddViewState : TextInputViewState("Input Todo Description")
data class TodoEditViewState(val editTarget: TodoItem, override val hintText: String = editTarget.description) :
    TextInputViewState(hintText)

data class TodoItem(val uniqueIndex: Int, val description: String, val isDone: Boolean = false) {

    override fun equals(other: Any?): Boolean {
        if (other !is TodoItem) return false

        return uniqueIndex == other.uniqueIndex
    }

    override fun hashCode(): Int {
        return uniqueIndex % 31
    }
}