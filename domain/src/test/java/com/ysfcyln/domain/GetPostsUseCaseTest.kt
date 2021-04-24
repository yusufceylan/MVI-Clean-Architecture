package com.ysfcyln.domain

import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.ysfcyln.common.Resource
import com.ysfcyln.domain.repository.Repository
import com.ysfcyln.domain.usecase.GetPostsUseCase
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
class GetPostsUseCaseTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var repository: Repository

    private lateinit var getPostsUseCase : GetPostsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        // Create GetPostsUseCase before every test
        getPostsUseCase = GetPostsUseCase(
            repository = repository,
            dispatcher = mainCoroutineRule.dispatcher
        )
    }

    @Test
    fun test_get_posts_success() = runBlockingTest {

        val posts = TestDataGenerator.generatePosts()
        val postFlow = flowOf(Resource.Success(posts))

        // Given
        coEvery { repository.getPosts() } returns postFlow

        // When & Assertions
        val result = getPostsUseCase.execute(null)
        result.test {
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = (expected as Resource.Success).data
            Truth.assertThat(expected).isInstanceOf(Resource.Success::class.java)
            Truth.assertThat(expectedData).containsExactlyElementsIn(posts)
            expectComplete()
        }

        // Then
        coVerify { repository.getPosts() }

    }

    @Test
    fun test_get_posts_fail() = runBlockingTest {

        val errorFlow = flowOf(Resource.Error(Exception()))

        // Given
        coEvery { repository.getPosts() } returns errorFlow

        // When & Assertions
        val result = getPostsUseCase.execute(null)
        result.test {
            // Expect Resource.Error
            Truth.assertThat(expectItem()).isInstanceOf(Resource.Error::class.java)
            expectComplete()
        }

        // Then
        coVerify { repository.getPosts() }

    }
}