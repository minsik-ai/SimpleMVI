package com.devtrentoh.sampleapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devtrentoh.sampleapp.R
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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

    val selectTodoItemSubject = PublishSubject.create<MainIntent.SelectTodoIntent>()
    val toggleCheckDoneTodoSubject = PublishSubject.create<MainIntent.ToggleCheckDoneTodoIntent>()

    val openEditTodoSubject = PublishSubject.create<MainIntent.OpenEditTodoIntent>()
    val applyEditTodoSubject = PublishSubject.create<MainIntent.ApplyEditTodoIntent>()

    override fun intents() = Observable.merge(
        listOf(
            openAddTodoSubject,
            applyAddTodoSubject,
            selectTodoItemSubject,
            toggleCheckDoneTodoSubject,
            openEditTodoSubject,
            applyEditTodoSubject
        )
    )

    private val todoDataListSubject = PublishSubject.create<List<TodoItem>>()
    private val selectedTodoIndexSubject = PublishSubject.create<Int>()

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
        todoitem_list.apply {
            layoutManager = LinearLayoutManager(containerView?.context)
            adapter = TodoListAdapter(todoDataListSubject, selectedTodoIndexSubject,
                onClickAction = { todoItem, isSelected ->
                    if (!isSelected) selectTodoItemSubject.onNext(MainIntent.SelectTodoIntent(todoItem))
                    else toggleCheckDoneTodoSubject.onNext(MainIntent.ToggleCheckDoneTodoIntent(todoItem))
                }
            )
        }
    }
}

enum class ActionButton {
    OPEN_ADD_TODO,
    OPEN_EDIT_TODO,
    APPLY_ADD_TODO,
    APPLY_EDIT_TODO
}

private class TodoListAdapter(
    dataObservable: Observable<List<TodoItem>>, selectedIndexObservable: Observable<Int>,
    private val onClickAction: (TodoItem, Boolean) -> Unit
) : RecyclerView.Adapter<TodoViewHolder>() {
    private var todoItems = listOf<TodoItem>()

    private var selectedIndex = -1

    // TODO : Dispose when needed
    private val disposables = CompositeDisposable()

    init {
        disposables.addAll(
            dataObservable
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    todoItems = it
                    notifyDataSetChanged()
                },
            selectedIndexObservable
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val prevIndex = selectedIndex
                    selectedIndex = it
                    notifyItemChanged(prevIndex)
                    notifyItemChanged(selectedIndex)
                }
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val todoItemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false)

        return TodoViewHolder(todoItemView)
    }

    override fun getItemCount(): Int = todoItems.count()

    override fun onBindViewHolder(binder: TodoViewHolder, position: Int) {
        val isSelected = position == selectedIndex
        binder.bind(todoItems[position], isSelected, onClickAction)
    }

}

private class TodoViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val descriptionText = view.findViewById<TextView>(R.id.todo_description_text)
    private val checkBox = view.findViewById<CheckBox>(R.id.todo_done_check)

    fun bind(todoItem: TodoItem, isSelected: Boolean, onClickAction: (TodoItem, Boolean) -> Unit) {

        view.setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                if (isSelected) R.color.colorPrimaryDark else R.color.colorPrimary
            )
        )

        descriptionText.text = todoItem.description
        checkBox.isChecked = todoItem.isDone

        view.setOnClickListener {
            onClickAction(todoItem, isSelected)
        }
    }

}