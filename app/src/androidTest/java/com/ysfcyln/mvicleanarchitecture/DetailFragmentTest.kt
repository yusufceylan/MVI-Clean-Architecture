package com.ysfcyln.mvicleanarchitecture

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.ysfcyln.mvicleanarchitecture.di.RepositoryModule
import com.ysfcyln.mvicleanarchitecture.ui.detail.DetailFragment
import com.ysfcyln.mvicleanarchitecture.ui.detail.DetailFragmentArgs
import com.ysfcyln.mvicleanarchitecture.utils.TestDataGenerator
import com.ysfcyln.mvicleanarchitecture.utils.launchFragmentInHiltContainer
import com.ysfcyln.presentation.model.PostUiModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@ExperimentalSerializationApi
@MediumTest
@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
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

        // When
        val bundle = DetailFragmentArgs.Builder()
            .setPost(clickedItem)
            .build()
            .toBundle()

        val scenario = launchFragmentInHiltContainer<DetailFragment>(fragmentArgs = bundle)

        // Then
        onView(withId(R.id.rv_comments)).check(matches(isDisplayed()))
    }

}