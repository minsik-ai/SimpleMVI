package com.devtrentoh.sampleapp.ui.main

import com.devtrentoh.sampleapp.ui.main.mvi.components.TodoItem
import java.lang.NullPointerException

class MainModel {

    private var currentTodoItemList = mutableListOf<TodoItem>()

    private var editTarget: TodoItem? = null

    fun addItem(description: String): TodoItem {
        val newItem = TodoItem(TODO("Available index finder"), description)
        currentTodoItemList.add(newItem)
        syncDB()
        return newItem
    }

    fun setEditTarget(editItem: TodoItem) {
        editTarget = editItem
    }

    fun editItem(newDescription: String): TodoItem {
        val target = editTarget ?: throw NullPointerException("No Edit Target Set")

        val index = currentTodoItemList.indexOf(target)

        val newItem = target.copy(description = newDescription)
        currentTodoItemList[index] = newItem

        editTarget = null

        syncDB()
        return newItem
    }

    fun toggleDone(todoItem: TodoItem) {
        val target = todoItem ?: throw NullPointerException("No Edit Target Set")

        val index = currentTodoItemList.indexOf(target)

        currentTodoItemList[index] = target.copy(isDone = !target.isDone)
    }

    fun getItemList(): List<TodoItem> = currentTodoItemList


    private fun syncDB() {
        // TODO()
    }
}