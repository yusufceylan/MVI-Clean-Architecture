package com.ysfcyln.mvicleanarchitecture.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import androidx.navigation.fragment.navArgs
import com.ysfcyln.base.BaseFragment
import com.ysfcyln.mvicleanarchitecture.databinding.FragmentDetailBinding
import com.ysfcyln.presentation.contract.DetailContract
import com.ysfcyln.presentation.vm.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

/**
 * Detail Fragment
 */
@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    private val viewModel : DetailViewModel by viewModels()
    private val args : DetailFragmentArgs by navArgs()
    private val adapter : CommentAdapter by lazy {
        CommentAdapter()
    }

    override val bindLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDetailBinding
        get() = FragmentDetailBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {
        binding.rvComments.adapter = adapter
        initObservers()
        // Fetch post comments only once
        if (viewModel.currentState.commentsState is DetailContract.CommentsState.Idle)
            viewModel.setEvent(DetailContract.Event.OnFetchPostComments(post = args.post))
    }

    /**
     * Initialize Observers
     */
    private fun initObservers() {
        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                when (val state = it.commentsState) {
                    is DetailContract.CommentsState.Idle -> {
                        binding.loadingPb.isVisible = false
                    }
                    is DetailContract.CommentsState.Loading -> {
                        binding.loadingPb.isVisible = true
                    }
                    is DetailContract.CommentsState.Success -> {
                        val data = state.comments
                        adapter.submitList(data)
                        binding.loadingPb.isVisible = false
                    }
                }
            }
        }

        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
            viewModel.effect.collect {
                when (it) {
                    is DetailContract.Effect.ShowError -> {
                        val msg = it.message
                    }
                }
            }
        }
    }

}