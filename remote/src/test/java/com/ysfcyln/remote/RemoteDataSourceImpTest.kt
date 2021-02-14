package com.ysfcyln.remote

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.ysfcyln.data.repository.RemoteDataSource
import com.ysfcyln.remote.api.ApiService
import com.ysfcyln.remote.mapper.CommentNetworkDataMapper
import com.ysfcyln.remote.mapper.PostNetworkDataMapper
import com.ysfcyln.remote.source.RemoteDataSourceImp
import com.ysfcyln.remote.utils.TestDataGenerator
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@SmallTest
class RemoteDataSourceImpTest {

    @MockK
    private lateinit var apiService : ApiService
    private val postNetworkDataMapper = PostNetworkDataMapper()
    private val commentNetworkDataMapper = CommentNetworkDataMapper()

    private lateinit var remoteDataSource : RemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        // Create RemoteDataSourceImp before every test
        remoteDataSource = RemoteDataSourceImp(
            apiService = apiService,
            postMapper = postNetworkDataMapper,
            commentMapper = commentNetworkDataMapper
        )
    }

    @Test
    fun test_get_post_success() = runBlockingTest {

        val postsNetwork = TestDataGenerator.generatePosts()

        // Given
        coEvery { apiService.getPosts() } returns postsNetwork

        // When
        val result = remoteDataSource.getPosts()

        // Then
        coVerify { apiService.getPosts() }

        // Assertion
        val expected = postNetworkDataMapper.fromList(postsNetwork)
        Truth.assertThat(result).containsExactlyElementsIn(expected)
    }

    @Test(expected = Exception::class)
    fun test_get_post_fail() = runBlockingTest {

        // Given
        coEvery { apiService.getPosts() } throws Exception()

        // When
        remoteDataSource.getPosts()

        // Then
        coVerify { apiService.getPosts() }

    }

    @Test
    fun test_get_post_comments_success() = runBlockingTest {

        val commentsNetwork = TestDataGenerator.generatePostComments()

        // Given
        coEvery { apiService.getPostComments(any()) } returns commentsNetwork

        // When
        val result = remoteDataSource.getPostComments(1)

        // Then
        coVerify { apiService.getPostComments(any()) }

        // Assertion
        val expected = commentNetworkDataMapper.fromList(commentsNetwork)
        Truth.assertThat(result).containsExactlyElementsIn(expected)
    }

    @Test(expected = Exception::class)
    fun test_get_post_comments_fail() = runBlockingTest {

        // Given
        coEvery { apiService.getPostComments(any()) } throws Exception()

        // When
        remoteDataSource.getPostComments(1)

        // Then
        coVerify { apiService.getPostComments(any()) }

    }
}