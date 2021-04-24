package com.ysfcyln.presentation.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ysfcyln.base.BaseViewModel
import com.ysfcyln.common.Mapper
import com.ysfcyln.common.Resource
import com.ysfcyln.domain.entity.CommentEntityModel
import com.ysfcyln.domain.usecase.GetPostCommentsUseCase
import com.ysfcyln.presentation.contract.DetailContract
import com.ysfcyln.presentation.model.CommentUiModel
import com.ysfcyln.presentation.model.PostUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPostCommentsUseCase: GetPostCommentsUseCase,
    private val commentMapper : Mapper<CommentEntityModel, CommentUiModel>
) : BaseViewModel<DetailContract.Event, DetailContract.State, DetailContract.Effect>() {

    override fun createInitialState(): DetailContract.State {
        return DetailContract.State(
            commentsState = DetailContract.CommentsState.Idle
        )
    }

    override fun handleEvent(event: DetailContract.Event) {
        when (event) {
            is DetailContract.Event.OnFetchPostComments -> {
                val post = event.post
                fetchPostComments(post = post)
            }
        }
    }

    /**
     * Fetch Post Comments
     */
    private fun fetchPostComments(post : PostUiModel?) {
        viewModelScope.launch {
            getPostCommentsUseCase.execute(post?.id)
                .onStart { emit(Resource.Loading) }
                .collect {
                    when (it) {
                        is Resource.Loading -> {
                            // Set state
                            setState { copy(commentsState = DetailContract.CommentsState.Loading) }
                        }
                        is Resource.Empty -> {
                            // Set state
                            setState { copy(commentsState = DetailContract.CommentsState.Idle) }
                        }
                        is Resource.Success -> {
                            // Set state
                            val comments = it.data
                            setState { copy(commentsState = DetailContract.CommentsState.Success(commentMapper.fromList(list = comments))) }
                        }
                        is Resource.Error -> {
                            // Set Effect
                            setEffect { DetailContract.Effect.ShowError(message = it.exception.message) }
                        }
                    }
                }
        }
    }
}