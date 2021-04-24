package com.ysfcyln.domain

import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.ysfcyln.common.Resource
import com.ysfcyln.domain.repository.Repository
import com.ysfcyln.domain.usecase.GetPostCommentsUseCase
import com.ysfcyln.domain.utils.MainCoroutineRule
import com.ysfcyln.domain.utils.TestDataGenerator
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
class GetPostCommentsUseCaseTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var repository: Repository

    private lateinit var getPostCommentsUseCase: GetPostCommentsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        // Create GetPostsUseCase before every test
        getPostCommentsUseCase = GetPostCommentsUseCase(
            repository = repository,
            dispatcher = mainCoroutineRule.dispatcher
        )
    }

    @Test
    fun test_get_post_comments_success() = runBlockingTest {

        val comments = TestDataGenerator.generatePostComments()
        val commentsFlow = flowOf(Resource.Success(comments))

        // Given
        coEvery { repository.getPostComments(any()) } returns commentsFlow

        // When & Assertions
        val result = getPostCommentsUseCase.execute(1)
        result.test {
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = (expected as Resource.Success).data
            Truth.assertThat(expected).isInstanceOf(Resource.Success::class.java)
            Truth.assertThat(expectedData).containsExactlyElementsIn(comments)
            expectComplete()
        }

        // Then
        coVerify { repository.getPostComments(any()) }

    }

    @Test
    fun test_get_post_comments_fail() = runBlockingTest {

        val errorFlow = flowOf(Resource.Error(Exception()))

        // Given
        coEvery { repository.getPostComments(any()) } returns errorFlow

        // When & Assertions
        val result = getPostCommentsUseCase.execute(1)
        result.test {
            // Expect Resource.Error
            Truth.assertThat(expectItem()).isInstanceOf(Resource.Error::class.java)
            expectComplete()
        }

        // Then
        coVerify { repository.getPostComments(any()) }

    }

    @Test
    fun test_get_post_comments_fail_with_empty_param() = runBlockingTest {

        val errorFlow = flowOf(Resource.Error(Exception()))

        // Given
        coEvery { repository.getPostComments(any()) } returns errorFlow

        // When & Assertions
        val result = getPostCommentsUseCase.execute(null)
        result.test {
            // Expect Resource.Error
            val expected = expectItem()
            val expectedData = (expected as Resource.Error).exception.message
            Truth.assertThat(expected).isInstanceOf(Resource.Error::class.java)
            Truth.assertThat(expectedData).isEqualTo("PostId can not be null")
            expectComplete()
        }

        // Then
        coVerify(exactly = 0) { repository.getPostComments(any()) }

    }

}