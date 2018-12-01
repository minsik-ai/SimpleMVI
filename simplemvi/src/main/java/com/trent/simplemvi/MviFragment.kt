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

package com.trent.simplemvi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.trent.simplemvi.mvi.MviProcessorHolder
import com.trent.simplemvi.mvi.MviReducerHolder
import com.trent.simplemvi.mvi.components.MviIntent
import com.trent.simplemvi.mvi.components.MviResult
import com.trent.simplemvi.mvi.components.MviViewState

abstract class MviFragment<I : MviIntent, R : MviResult, S : MviViewState, VM : MviViewModel<I, R, S>>(
    private val layoutId: Int,
    private val viewModelClass: Class<VM>,
    private val lazyProcessorHolder: () -> MviProcessorHolder<I, R>,
    private val lazyReducerHolder: () -> MviReducerHolder<R, S>,
    private val lazyView: (fragment: MviFragment<I, R, S, VM>, viewModel: VM) -> MviView<I, R, S>
) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(layoutId, container, false)

    private lateinit var viewModel: VM

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(
            this,
            MviViewModelFactory(lazyProcessorHolder(), lazyReducerHolder())
        ).get(viewModelClass)
        lazyView(this, viewModel).apply {
            lifecycle.addObserver(this)
        }
    }
}