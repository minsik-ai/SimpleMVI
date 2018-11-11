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

package com.devtrentoh.sampleapp.ui.main

import com.devtrentoh.sampleapp.ui.main.mvi.components.TodoItem
import java.lang.NullPointerException

class MainModel {

    private var currentTodoItemList = mutableListOf<TodoItem>()

    fun addItem(description: String): TodoItem {
        val newItem = TodoItem(findAvailableIndex(), description)
        currentTodoItemList.add(newItem)
        syncDB()
        return newItem
    }

    fun editItem(editTarget: TodoItem, newDescription: String): TodoItem {
        val index = currentTodoItemList.indexOf(editTarget)

        val newItem = editTarget.copy(description = newDescription)
        currentTodoItemList[index] = newItem

        syncDB()
        return newItem
    }

    fun deleteItem(todoItem: TodoItem) {
        currentTodoItemList.remove(todoItem)
        syncDB()
    }

    fun toggleDone(todoItem: TodoItem) {
        val index = currentTodoItemList.indexOf(todoItem)

        currentTodoItemList[index] = todoItem.copy(isDone = !todoItem.isDone)

        syncDB()
    }

    fun getItemList(): List<TodoItem> = currentTodoItemList

    private fun findAvailableIndex(): Int {
        val sortedTodoItems = currentTodoItemList.toList().sortedBy { it.uniqueIndex }
        var index = 0
        for (todoItem in sortedTodoItems) {
            if (todoItem.uniqueIndex != index) return index
            index++
        }
        return index
    }

    private fun syncDB() {
        // TODO("Make this asychronous but ordered")
    }
}