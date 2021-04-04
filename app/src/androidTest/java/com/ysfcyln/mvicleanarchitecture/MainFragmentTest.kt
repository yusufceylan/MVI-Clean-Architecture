package com.ysfcyln.mvicleanarchitecture

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import com.google.common.truth.Truth
import com.ysfcyln.mvicleanarchitecture.di.RepositoryModule
import com.ysfcyln.mvicleanarchitecture.ui.main.MainFragment
import com.ysfcyln.mvicleanarchitecture.ui.main.MainFragmentDirections
import com.ysfcyln.mvicleanarchitecture.ui.main.PostViewHolder
import com.ysfcyln.mvicleanarchitecture.utils.TestDataGenerator
import com.ysfcyln.mvicleanarchitecture.utils.launchFragmentInHiltContainer
import com.ysfcyln.presentation.model.PostUiModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@ExperimentalSerializationApi
@MediumTest
@HiltAndroidTest
class MainFragmentTest {


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
    fun test_recycler_view_item_click_with_mockk(){

        val navController = mockk<NavController>(relaxed = true)


        val scenario = launchFragmentInHiltContainer<MainFragment> {
            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(this.requireView(), navController)
                }
            }
        }

        // Check RecyclerView Item click
        onView(withId(R.id.rv_posts)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PostViewHolder>(
                0,
                click()
            )
        )

        val entityItem = TestDataGenerator.generatePosts().first()
        val item = PostUiModel(
            userId = entityItem.userId,
            id = entityItem.id,
            title = entityItem.title,
            body = entityItem.body
        )

        // Assertions
        verify { navController.navigate(MainFragmentDirections.actionMainFragmentToDetailFragment().setPost(item)) }
    }

    /**
     * Test navigation with Navigation Testing Library
     */
    @Test
    fun test_recycler_view_item_click_with_navigation_testing_library(){

        // Create a TestNavHostController
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext())

        // This needed because it throws a exception that method addObserver must be called in main thread
        UiThreadStatement.runOnUiThread {
            navController.setGraph(R.navigation.core_nav_graph)
        }

        val scenario = launchFragmentInHiltContainer<MainFragment> {
            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(this.requireView(), navController)
                }
            }
        }


        // Check RecyclerView Item click
        onView(withId(R.id.rv_posts)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PostViewHolder>(
                0,
                click()
            )
        )

        // Expected item id that comes from fake repository
        val expectedItemId = TestDataGenerator.generatePosts().first().id

        // Get the arguments from the last destination on the back stack
        val currentDestinationArgs = navController.backStack.last().arguments
        val postArg = currentDestinationArgs?.getParcelable<PostUiModel>("post")

        // Assertions
        Truth.assertThat(navController.currentDestination?.id).isEqualTo(R.id.detailFragment)
        Truth.assertThat(postArg?.id).isEqualTo(expectedItemId)
    }

}