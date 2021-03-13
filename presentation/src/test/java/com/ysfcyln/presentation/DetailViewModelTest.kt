package com.ysfcyln.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.ysfcyln.common.Resource
import com.ysfcyln.domain.usecase.GetPostCommentsUseCase
import com.ysfcyln.presentation.contract.DetailContract
import com.ysfcyln.presentation.mapper.CommentDomainUiMapper
import com.ysfcyln.presentation.utils.MainCoroutineRule
import com.ysfcyln.presentation.utils.TestDataGenerator
import com.ysfcyln.presentation.vm.DetailViewModel
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
class DetailViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var getPostCommentsUseCase: GetPostCommentsUseCase

    private val commentMapper = CommentDomainUiMapper()

    private lateinit var detailViewModel : DetailViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        // Create DetailViewModel before every test
        detailViewModel = DetailViewModel(
            savedStateHandle = savedStateHandle,
            getPostCommentsUseCase = getPostCommentsUseCase,
            commentMapper = commentMapper
        )
    }

    @Test
    fun test_fetch_post_comment_success() = runBlockingTest {

        val post = TestDataGenerator.generatePostUiModel()
        val comments = TestDataGenerator.generatePostComments()
        val commentsFlow = flowOf(Resource.Success(comments))

        // Given
        coEvery { getPostCommentsUseCase.execute(any()) } returns commentsFlow

        // When && Assertions
        detailViewModel.uiState.test {
            // Call method inside of this scope
            // For more info, see https://github.com/cashapp/turbine/issues/19
            detailViewModel.setEvent(DetailContract.Event.OnFetchPostComments(post = post))
            // Expect Resource.Idle from initial state
            Truth.assertThat(expectItem()).isEqualTo(
                DetailContract.State(
                    commentsState = DetailContract.CommentsState.Idle,
                )
            )
            // Expect Resource.Loading
            Truth.assertThat(expectItem()).isEqualTo(
                DetailContract.State(
                    commentsState = DetailContract.CommentsState.Loading,
                )
            )
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = (expected.commentsState as DetailContract.CommentsState.Success).comments
            Truth.assertThat(expected).isEqualTo(
                DetailContract.State(
                    commentsState = DetailContract.CommentsState.Success(comments = commentMapper.fromList(comments))
                )
            )
            Truth.assertThat(expectedData).containsExactlyElementsIn(commentMapper.fromList(comments))
            // Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }

        // Then
        coVerify { getPostCommentsUseCase.execute(any()) }

    }

    @Test
    fun test_fetch_post_comments_fail() = runBlockingTest {

        val post = TestDataGenerator.generatePostUiModel()
        val commentsErrorFlow = flowOf(Resource.Error(Exception("error string")))

        // Given
        coEvery { getPostCommentsUseCase.execute(any()) } returns commentsErrorFlow

        // When && Assertions (UiState)
        detailViewModel.uiState.test {
            // Call method inside of this scope
            // For more info, see https://github.com/cashapp/turbine/issues/19
            detailViewModel.setEvent(DetailContract.Event.OnFetchPostComments(post = post))
            // Expect Resource.Idle from initial state
            Truth.assertThat(expectItem()).isEqualTo(
                DetailContract.State(
                    commentsState = DetailContract.CommentsState.Idle,
                )
            )
            // Expect Resource.Loading
            Truth.assertThat(expectItem()).isEqualTo(
                DetailContract.State(
                    commentsState = DetailContract.CommentsState.Loading,
                )
            )
            // Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }

        // When && Assertions (UiEffect)
        detailViewModel.effect.test {
            // Expect ShowError Effect
            val expected = expectItem()
            val expectedData = (expected as DetailContract.Effect.ShowError).message
            Truth.assertThat(expected).isEqualTo(
                DetailContract.Effect.ShowError("error string")
            )
            Truth.assertThat(expectedData).isEqualTo("error string")
            // Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }

        // Then
        coVerify { getPostCommentsUseCase.execute(any()) }

    }

    @Test
    fun test_fetch_post_comments_fail_with_empty_post() = runBlockingTest {

        val commentsErrorFlow = flowOf(Resource.Error(Exception("PostId can not be null")))

        // Given
        coEvery { getPostCommentsUseCase.execute(any()) } returns commentsErrorFlow

        // When && Assertions (UiState)
        detailViewModel.uiState.test {
            // Call method inside of this scope
            // For more info, see https://github.com/cashapp/turbine/issues/19
            detailViewModel.setEvent(DetailContract.Event.OnFetchPostComments(null))
            // Expect Resource.Idle from initial state
            Truth.assertThat(expectItem()).isEqualTo(
                DetailContract.State(
                    commentsState = DetailContract.CommentsState.Idle,
                )
            )
            // Expect Resource.Loading
            Truth.assertThat(expectItem()).isEqualTo(
                DetailContract.State(
                    commentsState = DetailContract.CommentsState.Loading,
                )
            )
            // Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }

        // When && Assertions (UiEffect)
        detailViewModel.effect.test {
            // Expect ShowError Effect
            val expected = expectItem()
            val expectedData = (expected as DetailContract.Effect.ShowError).message
            Truth.assertThat(expected).isEqualTo(
                DetailContract.Effect.ShowError("PostId can not be null")
            )
            Truth.assertThat(expectedData).isEqualTo("PostId can not be null")
            // Cancel and ignore remaining
            cancelAndIgnoreRemainingEvents()
        }

        // Then
        coVerify { getPostCommentsUseCase.execute(null) }

    }
}