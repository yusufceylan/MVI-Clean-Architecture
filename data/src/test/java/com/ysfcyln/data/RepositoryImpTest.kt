package com.ysfcyln.data

import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.ysfcyln.common.Resource
import com.ysfcyln.data.mapper.CommentDataDomainMapper
import com.ysfcyln.data.mapper.PostDataDomainMapper
import com.ysfcyln.data.repository.LocalDataSource
import com.ysfcyln.data.repository.RemoteDataSource
import com.ysfcyln.data.repository.RepositoryImp
import com.ysfcyln.data.utils.TestDataGenerator
import com.ysfcyln.domain.repository.Repository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
@SmallTest
class RepositoryImpTest {

    @MockK
    private lateinit var localDataSource: LocalDataSource
    @MockK
    private lateinit var remoteDataSource: RemoteDataSource

    private val postMapper = PostDataDomainMapper()
    private val commentMapper = CommentDataDomainMapper()

    private lateinit var repository : Repository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        // Create RepositoryImp before every test
        repository = RepositoryImp(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            postMapper = postMapper,
            commentMapper = commentMapper
        )
    }

    @Test
    fun test_get_posts_remote_success() = runBlockingTest {

        val posts = TestDataGenerator.generatePosts()
        val affectedIds = MutableList(posts.size) { index -> index.toLong() }

        // Given
        coEvery { remoteDataSource.getPosts() } returns posts
        coEvery { localDataSource.addPostItems(posts) } returns affectedIds

        // When & Assertions
        val flow = repository.getPosts()
        flow.test {
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = (expected as Resource.Success).data
            Truth.assertThat(expected).isInstanceOf(Resource.Success::class.java)
            Truth.assertThat(expectedData).containsExactlyElementsIn(postMapper.fromList(posts))
            expectComplete()
        }

        // Then
        coVerify { remoteDataSource.getPosts() }
        coVerify { localDataSource.addPostItems(posts) }
    }

    @Test
    fun test_get_posts_remote_fail_local_success() = runBlockingTest {

        val posts = TestDataGenerator.generatePosts()

        // Given
        coEvery { remoteDataSource.getPosts() } throws Exception()
        coEvery { localDataSource.getPostItems() } returns posts

        // When && Assertions
        val flow = repository.getPosts()
        flow.test {
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = (expected as Resource.Success).data
            Truth.assertThat(expected).isInstanceOf(Resource.Success::class.java)
            Truth.assertThat(expectedData).containsExactlyElementsIn(postMapper.fromList(posts))
            expectComplete()
        }

        // Then
        coVerify { remoteDataSource.getPosts() }
        coVerify { localDataSource.getPostItems() }

    }

    @Test
    fun test_get_posts_remote_fail_local_fail() = runBlockingTest {


        // Given
        coEvery { remoteDataSource.getPosts() } throws Exception()
        coEvery { localDataSource.getPostItems() } throws Exception()

        // When && Assertions
        val flow = repository.getPosts()
        flow.test {
            // Expect Resource.Error
            Truth.assertThat(expectItem()).isInstanceOf(Resource.Error::class.java)
            expectComplete()
        }

        // Then
        coVerify { remoteDataSource.getPosts() }
        coVerify { localDataSource.getPostItems() }

    }

    @Test
    fun test_get_comments_success() = runBlockingTest {

        val comments = TestDataGenerator.generatePostComments()

        // Given
        coEvery { remoteDataSource.getPostComments(any()) } returns comments

        // When & Assertions
        val flow = repository.getPostComments(1)
        flow.test {
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = (expected as Resource.Success).data
            Truth.assertThat(expected).isInstanceOf(Resource.Success::class.java)
            Truth.assertThat(expectedData).containsExactlyElementsIn(commentMapper.fromList(comments))
            expectComplete()
        }

        // Then
        coVerify { remoteDataSource.getPostComments(any()) }
    }

    @Test
    fun test_get_comments_fail() = runBlockingTest {

        // Given
        coEvery { remoteDataSource.getPostComments(any()) } throws Exception()

        // When & Assertions
        val flow = repository.getPostComments(1)
        flow.test {
            // Expect Resource.Error
            Truth.assertThat(expectItem()).isInstanceOf(Resource.Error::class.java)
            expectComplete()
        }

        // Then
        coVerify { remoteDataSource.getPostComments(any()) }
    }
}