package com.devtrentoh.sampleapp.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devtrentoh.sampleapp.R
import com.devtrentoh.sampleapp.ui.main.mvi.MainProcessorHolder
import com.devtrentoh.sampleapp.ui.main.mvi.MainReducerHolder
import com.trent.simplemvi.MviViewModelFactory

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(
            this,
            MviViewModelFactory(
                MainProcessorHolder(MainModel()),
                MainReducerHolder()
            )
        ).get(MainViewModel::class.java)
        MainView(this.activity, this.view, viewModel).apply {
            lifecycle.addObserver(this)
            setup()
        }
    }

}
