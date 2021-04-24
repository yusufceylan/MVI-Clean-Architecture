package com.ysfcyln.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.ysfcyln.local.database.AppDatabase
import com.ysfcyln.local.database.PostDAO
import com.ysfcyln.local.utils.TestDataGenerator
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class PostDAOTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database : AppDatabase
    private lateinit var postDao : PostDAO

    @Before
    fun setUp() {
        hiltRule.inject()
        postDao = database.postDao()
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun test_add_post_item_success() = runBlockingTest {

        val item = TestDataGenerator.generatePostItem()

        postDao.addPostItem(item)

        val items = postDao.getPostItems()

        Truth.assertThat(items).contains(item)

    }

    @Test
    fun test_get_post_item_success() = runBlockingTest {

        val item = TestDataGenerator.generatePostItem()

        postDao.addPostItem(item)

        val result = postDao.getPostItem(item.id!!)

        Truth.assertThat(item).isEqualTo(result)
    }

    @Test
    fun test_add_and_get_post_items_success() = runBlockingTest {

        val items = TestDataGenerator.generatePosts()

        postDao.addPostItems(items)

        val result = postDao.getPostItems()

        Truth.assertThat(result).containsExactlyElementsIn(items)

    }

    @Test
    fun test_update_post_item_success() = runBlockingTest {

        val item = TestDataGenerator.generatePostItem()

        postDao.addPostItem(item)

        val dbItem = postDao.getPostItem(item.id!!)

        Truth.assertThat(item).isEqualTo(dbItem)

        val updatedItem = item.copy(body = "updated body")

        postDao.updatePostItem(updatedItem)

        val result = postDao.getPostItem(updatedItem.id!!)

        Truth.assertThat(updatedItem).isEqualTo(result)

    }

    @Test
    fun test_delete_post_item_by_id_success() = runBlockingTest {

        val item = TestDataGenerator.generatePostItem()

        postDao.addPostItem(item)

        val dbItem = postDao.getPostItem(item.id!!)

        Truth.assertThat(item).isEqualTo(dbItem)

        postDao.deletePostItemById(item.id!!)

        val items = postDao.getPostItems()

        Truth.assertThat(items).doesNotContain(item)

    }

    @Test
    fun test_delete_post_item_success() = runBlockingTest {

        val item = TestDataGenerator.generatePostItem()

        postDao.addPostItem(item)

        val dbItem = postDao.getPostItem(item.id!!)

        Truth.assertThat(item).isEqualTo(dbItem)

        postDao.deletePostItem(item)

        val items = postDao.getPostItems()

        Truth.assertThat(items).doesNotContain(item)

    }

    @Test
    fun test_clear_all_posts_success() = runBlockingTest {

        val items = TestDataGenerator.generatePosts()

        postDao.addPostItems(items)

        val dbItems = postDao.getPostItems()

        Truth.assertThat(dbItems).containsExactlyElementsIn(items)

        postDao.clearCachedPostItems()

        val result = postDao.getPostItems()

        Truth.assertThat(result).isEmpty()
    }
}