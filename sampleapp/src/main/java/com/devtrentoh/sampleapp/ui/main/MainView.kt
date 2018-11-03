package com.devtrentoh.sampleapp.ui.main

import android.view.View
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainIntent
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainResult
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainViewState
import com.trent.simplemvi.MviView
import io.reactivex.Observable
import kotlinx.android.extensions.LayoutContainer

class MainView(
    override val containerView: View?, private val viewModel: MainViewModel
) : MviView<MainIntent, MainResult, MainViewState>(viewModel), LayoutContainer {

    override fun intents() = TODO()

    override fun render(state: MainViewState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun setup() {

    }
}