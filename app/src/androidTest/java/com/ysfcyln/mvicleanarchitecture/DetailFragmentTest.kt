package com.ysfcyln.mvicleanarchitecture

import androidx.test.filters.MediumTest
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.ysfcyln.mvicleanarchitecture.ui.detail.DetailFragment
import com.ysfcyln.mvicleanarchitecture.ui.detail.DetailFragmentArgs
import com.ysfcyln.mvicleanarchitecture.utils.TestDataGenerator
import com.ysfcyln.mvicleanarchitecture.utils.launchFragmentInHiltContainer
import com.ysfcyln.presentation.model.PostUiModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@ExperimentalSerializationApi
@MediumTest
@HiltAndroidTest
class DetailFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Before
    fun setUp() {
        hiltRule.inject()
    }

    /**
     * Test navigation with MockK
     */
    @Test
    fun test_recycler_view_displayed_with_data() {

        // Given
        val entityItem = TestDataGenerator.generatePosts().first()
        val clickedItem = PostUiModel(
            userId = entityItem.userId,
            id = entityItem.id,
            title = entityItem.title,
            body = entityItem.body
        )
        val expectedItemCount = TestDataGenerator.generatePostComments().size

        // When
        val bundle = DetailFragmentArgs.Builder()
            .setPost(clickedItem)
            .build()
            .toBundle()

        val scenario = launchFragmentInHiltContainer<DetailFragment>(fragmentArgs = bundle)

        // Then
        assertDisplayed(R.id.rv_comments)
        assertRecyclerViewItemCount(R.id.rv_comments, expectedItemCount)
    }

}