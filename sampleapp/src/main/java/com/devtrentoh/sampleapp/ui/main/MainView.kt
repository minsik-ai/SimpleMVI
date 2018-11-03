package com.devtrentoh.sampleapp.ui.main

import android.view.View
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainIntent
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainResult
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainViewState
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainViewState.TextInputViewState
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainViewState.TextInputViewState.TodoAddViewState
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainViewState.TextInputViewState.TodoEditViewState
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainViewState.TodoListViewState
import com.devtrentoh.sampleapp.ui.main.mvi.components.TodoItem
import com.trent.simplemvi.MviView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.textinput_layout.*
import kotlinx.android.synthetic.main.todolist_layout.*
import java.lang.NullPointerException

class MainView(
    override val containerView: View?, private val viewModel: MainViewModel
) : MviView<MainIntent, MainResult, MainViewState>(viewModel), LayoutContainer {

    val openAddTodoSubject = PublishSubject.create<MainIntent.OpenAddTodoIntent>()
    val applyAddTodoSubject = PublishSubject.create<MainIntent.ApplyAddTodoIntent>()

    val selectTodoSubject = PublishSubject.create<MainIntent.SelectTodoIntent>()
    val checkDoneTodoSubject = PublishSubject.create<MainIntent.CheckDoneTodoIntent>()

    val openEditTodoSubject = PublishSubject.create<MainIntent.OpenEditTodoIntent>()
    val applyEditTodoSubject = PublishSubject.create<MainIntent.ApplyEditTodoIntent>()

//    val cancelModifyTodoListSubject = PublishSubject.create<MainIntent.CancelModifyTodoListIntent>()

    override fun intents() = Observable.merge(
        listOf(
            openAddTodoSubject,
            applyAddTodoSubject,
            selectTodoSubject,
            checkDoneTodoSubject,
            openEditTodoSubject,
            applyEditTodoSubject
//            cancelModifyTodoListSubject
        )
    )

    private var currentActionButton = ActionButton.OPEN_ADD_TODO

    private var prevState: MainViewState = viewModel.initialState

    override fun render(state: MainViewState) {
        todolist_layout.visibility = View.GONE
        textinput_layout.visibility = View.GONE

        when (state) {
            is TodoListViewState -> {
                todolist_layout.visibility = View.VISIBLE
                // TODO : Put items in recyclerview
                if (state.selectedItem != null) {
                    currentActionButton = ActionButton.OPEN_EDIT_TODO
                } else {
                    currentActionButton = ActionButton.OPEN_ADD_TODO
                }
            }
            is TextInputViewState -> {
                textinput_layout.visibility = View.GONE
                text_input.hint = state.hintText
                when (state) {
                    is TodoAddViewState -> currentActionButton = ActionButton.APPLY_ADD_TODO
                    is TodoEditViewState -> currentActionButton = ActionButton.APPLY_EDIT_TODO
                }
            }
        }

        // TODO : Set matching images
        when (currentActionButton) {
            ActionButton.OPEN_ADD_TODO -> TODO()
            ActionButton.OPEN_EDIT_TODO -> TODO()
            ActionButton.APPLY_ADD_TODO -> TODO()
            ActionButton.APPLY_EDIT_TODO -> TODO()
        }
    }

    fun setup() {
        bottom_action_button.setOnClickListener {
            when (currentActionButton) {
                ActionButton.OPEN_ADD_TODO -> openAddTodoSubject.onNext(MainIntent.OpenAddTodoIntent)
                ActionButton.OPEN_EDIT_TODO -> {
                    val selectedItem =
                        (prevState as TodoListViewState).selectedItem ?: throw NullPointerException("No Selected Item")

                    openEditTodoSubject.onNext(MainIntent.OpenEditTodoIntent(selectedItem))
                }
                ActionButton.APPLY_ADD_TODO -> {
                    applyAddTodoSubject.onNext(MainIntent.ApplyAddTodoIntent(text_input.text.toString()))
                }
                ActionButton.APPLY_EDIT_TODO -> {
                    applyEditTodoSubject.onNext(MainIntent.ApplyEditTodoIntent(text_input.text.toString()))
                }
            }
        }
    }
}

enum class ActionButton {
    OPEN_ADD_TODO,
    OPEN_EDIT_TODO,
    APPLY_ADD_TODO,
    APPLY_EDIT_TODO
}