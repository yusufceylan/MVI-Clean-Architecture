package com.ysfcyln.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.ysfcyln.common.Resource
import com.ysfcyln.domain.usecase.GetPostsUseCase
import com.ysfcyln.presentation.contract.MainContract
import com.ysfcyln.presentation.mapper.PostDomainUiMapper
import com.ysfcyln.presentation.utils.MainCoroutineRule
import com.ysfcyln.presentation.utils.TestDataGenerator
import com.ysfcyln.presentation.vm.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
@SmallTest
class MainViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var postUseCase: GetPostsUseCase

    private val postMapper = PostDomainUiMapper()

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        // Create MainViewModel before every test
        mainViewModel = MainViewModel(
            savedStateHandle = savedStateHandle,
            postsUseCase = postUseCase,
            postMapper = postMapper
        )
    }

    @Test
    fun test_fetch_posts_success() = runBlockingTest {

        val posts = TestDataGenerator.generatePosts()
        val postFlow = flowOf(Resource.Success(posts))

        // Given
        coEvery { postUseCase.execute(null) } returns postFlow

        // When && Assertions
        mainViewModel.uiState.test {
            // Call method inside of this scope
            // For more info, see https://github.com/cashapp/turbine/issues/19
            mainViewModel.setEvent(MainContract.Event.OnFetchPosts)
            // Expect Resource.Idle from initial state
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    postsState = MainContract.PostsState.Idle,
                    selectedPost = null
                )
            )
            // Expect Resource.Loading
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    postsState = MainContract.PostsState.Loading,
                    selectedPost = null
                )
            )
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = (expected.postsState as MainContract.PostsState.Success).posts
            Truth.assertThat(expected).isEqualTo(
                MainContract.State(
                    postsState = MainContract.PostsState.Success(postMapper.fromList(posts)),
                    selectedPost = null
                )
            )
            Truth.assertThat(expectedData).containsExactlyElementsIn(postMapper.fromList(posts))
            // Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }


        // Then
        coVerify { postUseCase.execute(null) }
    }

    @Test
    fun test_fetch_posts_fail() = runBlockingTest {

        val postErrorFlow = flowOf(Resource.Error(Exception("error string")))

        // Given
        coEvery { postUseCase.execute(null) } returns postErrorFlow

        // When && Assertions (UiState)
        mainViewModel.uiState.test {
            // Call method inside of this scope
            // For more info, see https://github.com/cashapp/turbine/issues/19
            mainViewModel.setEvent(MainContract.Event.OnFetchPosts)
            // Expect Resource.Idle from initial state
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    postsState = MainContract.PostsState.Idle,
                    selectedPost = null
                )
            )
            // Expect Resource.Loading
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    postsState = MainContract.PostsState.Loading,
                    selectedPost = null
                )
            )
            // Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }

        // When && Assertions (UiEffect)
        mainViewModel.effect.test {
            // Expect ShowError Effect
            val expected = expectItem()
            val expectedData = (expected as MainContract.Effect.ShowError).message
            Truth.assertThat(expected).isEqualTo(
                MainContract.Effect.ShowError("error string")
            )
            Truth.assertThat(expectedData).isEqualTo("error string")
            // Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }


        // Then
        coVerify { postUseCase.execute(null) }
    }

    @Test
    fun test_select_post_item() = runBlockingTest {

        val post = TestDataGenerator.generatePosts().first()

        // Given (no-op)

        // When && Assertions
        mainViewModel.uiState.test {
            // Call method inside of this scope
            // For more info, see https://github.com/cashapp/turbine/issues/19
            mainViewModel.setEvent(MainContract.Event.OnPostItemClicked(post = postMapper.from(post)))
            // Expect Resource.Idle from initial state
            Truth.assertThat(expectItem()).isEqualTo(
                MainContract.State(
                    postsState = MainContract.PostsState.Idle,
                    selectedPost = null
                )
            )
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = expected.selectedPost
            Truth.assertThat(expected).isEqualTo(
                MainContract.State(
                    postsState = MainContract.PostsState.Idle,
                    selectedPost = postMapper.from(post)
                )
            )
            Truth.assertThat(expectedData).isEqualTo(postMapper.from(post))
            // Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }


        // Then (no-op)
    }
}