package com.ysfcyln.presentation.contract

import com.ysfcyln.base.UiEffect
import com.ysfcyln.base.UiEvent
import com.ysfcyln.base.UiState
import com.ysfcyln.presentation.model.PostUiModel

/**
 * Contract of Main Screen
 */
class MainContract {

    sealed class Event : UiEvent {
        object OnFetchPosts : Event()
        data class OnPostItemClicked(val post : PostUiModel) : Event()
    }

    data class State(
        val postsState: PostsState,
        val selectedPost: PostUiModel? = null
    ) : UiState

    sealed class PostsState {
        object Idle : PostsState()
        object Loading : PostsState()
        data class Success(val posts : List<PostUiModel>) : PostsState()
    }

    sealed class Effect : UiEffect {

        data class ShowError(val message : String?) : Effect()

    }

}