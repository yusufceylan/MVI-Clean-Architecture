package com.ysfcyln.presentation.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ysfcyln.base.BaseViewModel
import com.ysfcyln.common.Mapper
import com.ysfcyln.common.Resource
import com.ysfcyln.domain.entity.PostEntityModel
import com.ysfcyln.domain.usecase.GetPostsUseCase
import com.ysfcyln.presentation.contract.MainContract
import com.ysfcyln.presentation.model.PostUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val postsUseCase: GetPostsUseCase,
    private val postMapper : Mapper<PostEntityModel, PostUiModel>
) : BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>(){

    override fun createInitialState(): MainContract.State {
        return MainContract.State(
            postsState = MainContract.PostsState.Idle,
            selectedPost = null
        )
    }

    override fun handleEvent(event: MainContract.Event) {
        when (event) {
            is MainContract.Event.OnFetchPosts -> {
                fetchPosts()
            }
            is MainContract.Event.OnPostItemClicked -> {
                val item = event.post
                setSelectedPost(post = item)
            }
        }
    }

    /**
     * Fetch posts
     */
    private fun fetchPosts() {
        viewModelScope.launch {
            postsUseCase.execute(null)
                .onStart { emit(Resource.Loading) }
                .collect {
                    when (it) {
                        is Resource.Loading -> {
                            // Set State
                            setState { copy(postsState = MainContract.PostsState.Loading) }
                        }
                        is Resource.Empty -> {
                            // Set State
                            setState { copy(postsState = MainContract.PostsState.Idle) }
                        }
                        is Resource.Success -> {
                            // Set State
                            val posts = postMapper.fromList(it.data)
                            setState { copy(postsState = MainContract.PostsState.Success(posts = posts)) }
                        }
                        is Resource.Error -> {
                            // Set Effect
                            setEffect { MainContract.Effect.ShowError(message = it.exception.message) }
                        }
                    }
                }
        }
    }

    /**
     * Set selected post item
     */
    private fun setSelectedPost(post : PostUiModel?) {
        // Set State
        setState { copy(selectedPost = post) }
    }
}