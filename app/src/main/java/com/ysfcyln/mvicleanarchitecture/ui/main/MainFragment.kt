package com.ysfcyln.mvicleanarchitecture.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ysfcyln.base.BaseFragment
import com.ysfcyln.mvicleanarchitecture.databinding.FragmentMainBinding
import com.ysfcyln.presentation.contract.MainContract
import com.ysfcyln.presentation.model.PostUiModel
import com.ysfcyln.presentation.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

/**
 * Main Fragment
 */
@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val viewModel : MainViewModel by viewModels()

    override val bindLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {
        initObservers()
        viewModel.setEvent(MainContract.Event.OnFetchPosts)

        binding.tvSample.setOnClickListener {
            val dummyPost = PostUiModel(1, 1, "title", "body")
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment().setPost(dummyPost)
            findNavController().navigate(action)
        }
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