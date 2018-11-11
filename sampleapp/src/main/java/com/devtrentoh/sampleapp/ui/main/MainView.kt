package com.devtrentoh.sampleapp.ui.main

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devtrentoh.sampleapp.R
import com.devtrentoh.sampleapp.ui.main.mvi.components.*
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
    private val activity: Activity?, override val containerView: View?, viewModel: MainViewModel
) : MviView<MainIntent, MainResult, MainViewState>(viewModel), LayoutContainer {

    private val todoDataListSubject = PublishSubject.create<List<TodoItem>>()
    private val selectedTodoItemSubject = PublishSubject.create<TodoItem>()

    private var currentActionButtonL = ActionButtonL.NOTHING
    private var currentActionButtonR = ActionButtonR.OPEN_ADD_TODO

    private var currentState: MainViewState = viewModel.initialState

    override fun render(state: MainViewState) {

        currentState = state

        todolist_layout.visibility = View.GONE
        textinput_layout.visibility = View.GONE

        val actionBarTitle = when (state) {
            is TodoListViewState -> "Todo List"
            TodoAddViewState -> "Add Todo"
            is TodoEditViewState -> "Edit Todo"
        }

        activity?.title = actionBarTitle

        when (state) {
            is TodoListViewState -> {

                Log.d("MainView", "New State : " + state)

                todolist_layout.visibility = View.VISIBLE

                if (state.selectedItem != null) {
                    currentActionButtonL = ActionButtonL.DELETE_TODO
                    currentActionButtonR = ActionButtonR.OPEN_EDIT_TODO
                } else {
                    currentActionButtonL = ActionButtonL.NOTHING
                    currentActionButtonR = ActionButtonR.OPEN_ADD_TODO
                }

                todoDataListSubject.onNext(state.todoList)

                selectedTodoItemSubject.onNext(state.selectedItem ?: TodoItem(-1, ""))
            }
            is TextInputViewState -> {
                textinput_layout.visibility = View.VISIBLE
                text_input.hint = state.hintText
                text_input.text.clear()
                currentActionButtonL = ActionButtonL.CANCEL_TODO
                currentActionButtonR = when (state) {
                    is TodoAddViewState -> ActionButtonR.APPLY_ADD_TODO
                    is TodoEditViewState -> ActionButtonR.APPLY_EDIT_TODO
                }
            }
        }

        val buttonLTextResId = when (currentActionButtonL) {
            ActionButtonL.NOTHING -> R.string.empty
            ActionButtonL.DELETE_TODO -> R.string.delete
            ActionButtonL.CANCEL_TODO -> R.string.cancel
        }


        val buttonRTextResId = when (currentActionButtonR) {
            ActionButtonR.OPEN_ADD_TODO -> R.string.add_todo
            ActionButtonR.OPEN_EDIT_TODO -> R.string.edit_todo
            ActionButtonR.APPLY_ADD_TODO -> R.string.finish_add
            ActionButtonR.APPLY_EDIT_TODO -> R.string.apply_edit
        }

        bottom_action_button_L.text = containerView?.context?.getText(buttonLTextResId)
        bottom_action_button_R.text = containerView?.context?.getText(buttonRTextResId)
    }

    fun setup() {
        bottom_action_button_L.setOnClickListener {
            when (currentActionButtonL) {
                ActionButtonL.NOTHING -> {
                }
                ActionButtonL.DELETE_TODO -> {
                    val selectedItem =
                        (currentState as TodoListViewState).selectedItem
                            ?: throw NullPointerException("No Selected Item")
                    intentsSubject.onNext(DeleteTodoIntent(selectedItem))
                }
                ActionButtonL.CANCEL_TODO -> intentsSubject.onNext(CancelTodoEditIntent)
            }
        }
        bottom_action_button_R.setOnClickListener {
            when (currentActionButtonR) {
                ActionButtonR.OPEN_ADD_TODO -> intentsSubject.onNext(OpenAddTodoIntent)
                ActionButtonR.OPEN_EDIT_TODO -> {
                    val selectedItem =
                        (currentState as TodoListViewState).selectedItem
                            ?: throw NullPointerException("No Selected Item")

                    intentsSubject.onNext(OpenEditTodoIntent(selectedItem))
                }
                ActionButtonR.APPLY_ADD_TODO -> {
                    intentsSubject.onNext(ApplyAddTodoIntent(text_input.text.toString()))
                }
                ActionButtonR.APPLY_EDIT_TODO -> {
                    val editTarget = (currentState as TodoEditViewState).editTarget
                    intentsSubject.onNext(ApplyEditTodoIntent(editTarget, text_input.text.toString()))
                }
            }
        }
        todoitem_list.apply {
            layoutManager = LinearLayoutManager(containerView?.context)
            adapter = TodoListAdapter(todoDataListSubject, selectedTodoItemSubject,
                onClickAction = { todoItem, isSelected ->
                    if (!isSelected) intentsSubject.onNext(SelectTodoIntent(todoItem))
                    else intentsSubject.onNext(ToggleDoneTodoIntent(todoItem))
                }
            )
        }
    }
}

private enum class ActionButtonL {
    NOTHING,
    DELETE_TODO,
    CANCEL_TODO
}

private enum class ActionButtonR {
    OPEN_ADD_TODO,
    OPEN_EDIT_TODO,
    APPLY_ADD_TODO,
    APPLY_EDIT_TODO
}

private class TodoListAdapter(
    dataObservable: Observable<List<TodoItem>>, selectedItemObservable: Observable<TodoItem>,
    private val onClickAction: (TodoItem, Boolean) -> Unit
) : RecyclerView.Adapter<TodoViewHolder>() {
    private var todoItems = listOf<TodoItem>()

    private var selectedPosition = -1

    // TODO : Dispose when needed
    private val disposables = CompositeDisposable()

    init {
        disposables.addAll(
            dataObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    todoItems = it
                    notifyDataSetChanged()
                },
            selectedItemObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val prevPosition = selectedPosition
                    selectedPosition = todoItems.indexOf(it)
                    notifyItemChanged(prevPosition)
                    notifyItemChanged(selectedPosition)
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
        val isSelected = position == selectedPosition
        binder.bind(todoItems[position], isSelected, onClickAction)
    }

}

private class TodoViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val descriptionText = view.findViewById<TextView>(R.id.todo_description_text)
    private val checkBox = view.findViewById<CheckBox>(R.id.todo_done_check)

    fun bind(todoItem: TodoItem, isSelected: Boolean, onClickAction: (TodoItem, Boolean) -> Unit) {

        view.setBackgroundColor(ContextCompat.getColor(view.context, if (isSelected) R.color.gray else R.color.white))

        descriptionText.text = todoItem.description
        checkBox.isChecked = todoItem.isDone

        view.setOnClickListener { onClickAction(todoItem, isSelected) }
    }

}