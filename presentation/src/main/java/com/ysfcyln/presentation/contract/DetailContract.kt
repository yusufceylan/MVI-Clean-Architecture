package com.ysfcyln.presentation.contract

import com.ysfcyln.base.UiEffect
import com.ysfcyln.base.UiEvent
import com.ysfcyln.base.UiState
import com.ysfcyln.presentation.model.CommentUiModel
import com.ysfcyln.presentation.model.PostUiModel

/**
 * Contract of Main Screen
 */
class DetailContract {

    sealed class Event : UiEvent {
        data class OnFetchPostComments(val post : PostUiModel?) : Event()
    }

    data class State(
        val commentsState: CommentsState,
    ) : UiState

    sealed class CommentsState {
        object Idle : CommentsState()
        object Loading : CommentsState()
        data class Success(val comments : List<CommentUiModel>) : CommentsState()
    }

    sealed class Effect : UiEffect {

        data class ShowError(val message : String?) : Effect()

    }

}