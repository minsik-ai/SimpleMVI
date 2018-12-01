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

import com.devtrentoh.sampleapp.R
import com.devtrentoh.sampleapp.ui.main.mvi.MainProcessorHolder
import com.devtrentoh.sampleapp.ui.main.mvi.MainReducerHolder
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainIntent
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainResult
import com.devtrentoh.sampleapp.ui.main.mvi.components.MainViewState
import com.trent.simplemvi.MviFragment

class MainFragment : MviFragment<MainIntent, MainResult, MainViewState, MainViewModel>(
    R.layout.main_fragment,
    MainViewModel::class.java,
    { MainProcessorHolder(MainModel()) },
    { MainReducerHolder() },
    { fragment, viewModel ->
        MainView(fragment.activity, fragment.view, viewModel).apply {
            setup()
        }
    }) {

    companion object {
        fun newInstance() = MainFragment()
    }
}
