package com.ysfcyln.mvicleanarchitecture.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ysfcyln.base.BaseFragment
import com.ysfcyln.mvicleanarchitecture.databinding.FragmentMainBinding
import com.ysfcyln.presentation.contract.MainContract
import com.ysfcyln.presentation.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

/**
 * Main Fragment
 */
@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val viewModel : MainViewModel by viewModels()
    private val adapter : PostAdapter by lazy {
        PostAdapter { post ->
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment().setPost(post)
            findNavController().navigate(action)
        }
    }

    override val bindLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {
        binding.rvPosts.adapter = adapter
        initObservers()
        // Fetch only once
        if (viewModel.currentState.postsState is MainContract.PostsState.Idle)
            viewModel.setEvent(MainContract.Event.OnFetchPosts)
    }

    /**
     * Initialize Observers
     */
    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                when (val state = it.postsState) {
                    is MainContract.PostsState.Idle -> {
                        binding.loadingPb.isVisible = false
                    }
                    is MainContract.PostsState.Loading -> {
                        binding.loadingPb.isVisible = true
                    }
                    is MainContract.PostsState.Success -> {
                        val data = state.posts
                        adapter.submitList(data)
                        binding.loadingPb.isVisible = false
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