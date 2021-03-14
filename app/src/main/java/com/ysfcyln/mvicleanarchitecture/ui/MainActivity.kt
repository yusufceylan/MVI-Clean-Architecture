package com.ysfcyln.mvicleanarchitecture.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.ysfcyln.base.BaseActivity
import com.ysfcyln.mvicleanarchitecture.databinding.ActivityMainBinding
import com.ysfcyln.presentation.contract.MainContract
import com.ysfcyln.presentation.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel : MainViewModel by viewModels()

    override val bindLayout: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {
        // init views
        initObservers()
        viewModel.handleEvent(MainContract.Event.OnFetchPosts)
    }

    /**
     * Initialize Observers
     */
    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                when (val state = it.postsState) {
                    is MainContract.PostsState.Idle -> {
                        Log.d("Ysf", "Idle")
                    }
                    is MainContract.PostsState.Loading -> {
                        Log.d("Ysf", "Loading")
                    }
                    is MainContract.PostsState.Success -> {
                        val data = state.posts
                        Log.d("Ysf", "Success")
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.effect.collect {
                when (it) {
                    is MainContract.Effect.ShowError -> {
                        val msg = it.message
                    }
                }
            }
        }
    }

}