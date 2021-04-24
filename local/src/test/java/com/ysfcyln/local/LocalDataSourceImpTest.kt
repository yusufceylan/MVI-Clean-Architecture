package com.ysfcyln.local

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.ysfcyln.data.repository.LocalDataSource
import com.ysfcyln.local.database.PostDAO
import com.ysfcyln.local.mapper.PostLocalDataMapper
import com.ysfcyln.local.source.LocalDataSourceImp
import com.ysfcyln.local.utils.TestData
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
class LocalDataSourceImpTest {

    @MockK
    private lateinit var postDao : PostDAO

    private val postLocalDataMapper = PostLocalDataMapper()
    private lateinit var localDataSource : LocalDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        // Create RemoteDataSourceImp before every test
        localDataSource = LocalDataSourceImp(
            postDAO = postDao,
            postMapper = postLocalDataMapper
        )
    }

    @Test
    fun test_add_post_item_success() = runBlockingTest {

        val postLocal = TestData.generatePostItem()
        val expected = 1L

        // Given
        coEvery { postDao.addPostItem(any()) } returns expected

        // When
        val returned = localDataSource.addPostItem(postLocalDataMapper.from(i = postLocal))

        // Then
        coVerify { postDao.addPostItem(any()) }

        // Assertion
        Truth.assertThat(returned).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun test_add_post_item_fail() = runBlockingTest {

        val postItem = postLocalDataMapper.from(i = TestData.generatePostItem())

        // Given
        coEvery { postDao.addPostItem(any()) } throws Exception()

        // When
        localDataSource.addPostItem(postItem)

        // Then
        coVerify { postDao.addPostItem(any()) }

    }

    @Test
    fun test_get_post_item_success() = runBlockingTest {

        val postLocal = TestData.generatePostItem()
        val expected = postLocalDataMapper.from(i = postLocal)

        // Given
        coEvery { postDao.getPostItem(any()) } returns postLocal

        // When
        val returned = localDataSource.getPostItem(postLocal.id!!)

        // Then
        coVerify { postDao.getPostItem(any()) }

        // Assertion
        Truth.assertThat(returned).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun test_get_post_item_fail() = runBlockingTest {

        val postItem = postLocalDataMapper.from(i = TestData.generatePostItem())

        // Given
        coEvery { postDao.getPostItem(any()) } throws Exception()

        // When
        localDataSource.getPostItem(postItem.id!!)

        // Then
        coVerify { postDao.getPostItem(any()) }

    }

    @Test
    fun test_add_post_items_success() = runBlockingTest {

        val postItems = postLocalDataMapper.fromList(list = TestData.generatePosts())
        val expected = MutableList(postItems.size) { index -> index.toLong() }

        // Given
        coEvery { postDao.addPostItems(any()) } returns expected

        // When
        val returned = localDataSource.addPostItems(postItems)

        // Then
        coVerify { postDao.addPostItems(any()) }

        // Assertion
        Truth.assertThat(returned).hasSize(expected.size)
    }

    @Test(expected = Exception::class)
    fun test_add_post_items_fail() = runBlockingTest {

        val postItems = postLocalDataMapper.fromList(list = TestData.generatePosts())

        // Given
        coEvery { postDao.addPostItems(any()) } throws Exception()

        // When
        localDataSource.addPostItems(postItems)

        // Then
        coVerify { postDao.addPostItems(any()) }

    }

    @Test
    fun test_get_post_items_success() = runBlockingTest {

        val postItems = TestData.generatePosts()
        val expected = postLocalDataMapper.fromList(list = postItems)

        // Given
        coEvery { postDao.getPostItems() } returns postItems

        // When
        val returned = localDataSource.getPostItems()

        // Then
        coVerify { postDao.getPostItems() }

        // Assertion
        Truth.assertThat(returned).containsExactlyElementsIn(expected)
    }

    @Test(expected = Exception::class)
    fun test_get_post_items_fail() = runBlockingTest {

        // Given
        coEvery { postDao.getPostItems() } throws Exception()

        // When
        localDataSource.getPostItems()

        // Then
        coVerify { postDao.getPostItems() }

    }

    @Test
    fun test_update_post_item_success() = runBlockingTest {

        val postItem = postLocalDataMapper.from(i = TestData.generatePostItem())
        val expected = 1

        // Given
        coEvery { postDao.updatePostItem(any()) } returns expected

        // When
        val returned = localDataSource.updatePostItem(postItem)

        // Then
        coVerify { postDao.updatePostItem(any()) }

        // Assertion
        Truth.assertThat(returned).isEqualTo(expected)

    }

    @Test(expected = Exception::class)
    fun test_update_post_item_fail() = runBlockingTest {

        val postItem = postLocalDataMapper.from(i = TestData.generatePostItem())

        // Given
        coEvery { postDao.updatePostItem(any()) } throws  Exception()

        // When
        localDataSource.updatePostItem(postItem)

        // Then
        coVerify { postDao.updatePostItem(any()) }

    }

    @Test
    fun test_delete_post_item_by_id_success() = runBlockingTest {

        val expected = 1

        // Given
        coEvery { postDao.deletePostItemById(any()) } returns expected

        // When
        val returned = localDataSource.deletePostItemById(1)

        // Then
        coVerify { postDao.deletePostItemById(any()) }

        // Assertion
        Truth.assertThat(returned).isEqualTo(expected)

    }

    @Test(expected = Exception::class)
    fun test_delete_post_item_by_id_fail() = runBlockingTest {

        // Given
        coEvery { postDao.deletePostItemById(any()) } throws Exception()

        // When
        localDataSource.deletePostItemById(1)

        // Then
        coVerify { postDao.deletePostItemById(any()) }

    }

    @Test
    fun test_delete_post_item_success() = runBlockingTest {

        val postItem = postLocalDataMapper.from(i = TestData.generatePostItem())
        val expected = 1

        // Given
        coEvery { postDao.deletePostItem(any()) } returns expected

        // When
        val returned = localDataSource.deletePostItem(postItem)

        // Then
        coVerify { postDao.deletePostItem(any()) }

        // Assertion
        Truth.assertThat(returned).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun test_delete_post_item_fail() = runBlockingTest {

        val postItem = postLocalDataMapper.from(i = TestData.generatePostItem())

        // Given
        coEvery { postDao.deletePostItem(any()) } throws Exception()

        // When
        localDataSource.deletePostItem(postItem)

        // Then
        coVerify { postDao.deletePostItem(any()) }

    }

    @Test
    fun test_clear_all_posts_success() = runBlockingTest {

        val postItems = postLocalDataMapper.fromList(list = TestData.generatePosts())
        val expected = postItems.size // Affected row count

        // Given
        coEvery { postDao.clearCachedPostItems() } returns expected

        // When
        val returned = localDataSource.clearCachedPostItems()

        // Then
        coVerify { postDao.clearCachedPostItems() }

        // Assertion
        Truth.assertThat(returned).isEqualTo(expected)

    }

    @Test(expected = Exception::class)
    fun test_clear_all_posts_fail() = runBlockingTest {

        // Given
        coEvery { postDao.clearCachedPostItems() } throws Exception()

        // When
        localDataSource.clearCachedPostItems()

        // Then
        coVerify { postDao.clearCachedPostItems() }

    }
}