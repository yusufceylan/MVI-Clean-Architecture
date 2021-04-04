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


/*
package com.ysfcyln.mvicleanarchitecture

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import com.google.common.truth.Truth
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.ysfcyln.mvicleanarchitecture.di.NetworkModule
import com.ysfcyln.mvicleanarchitecture.di.PersistenceModule
import com.ysfcyln.mvicleanarchitecture.ui.main.MainFragment
import com.ysfcyln.mvicleanarchitecture.ui.main.MainFragmentDirections
import com.ysfcyln.mvicleanarchitecture.ui.main.PostViewHolder
import com.ysfcyln.mvicleanarchitecture.utils.MockResponseFileReader
import com.ysfcyln.mvicleanarchitecture.utils.launchFragmentInHiltContainer
import com.ysfcyln.presentation.contract.MainContract
import com.ysfcyln.presentation.model.PostUiModel
import com.ysfcyln.presentation.vm.MainViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ExperimentalSerializationApi
@MediumTest
@HiltAndroidTest
@UninstallModules(PersistenceModule::class, NetworkModule::class)
class MainFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val mockWebServer = MockWebServer()

    /*
    @Inject
    lateinit var okHttpClient : OkHttpClient
     */

    @Inject
    lateinit var okHttp3IdlingResource: OkHttp3IdlingResource

    @Before
    fun setUp() {
        hiltRule.inject()
        // Prepare Mock Web Server
        mockWebServer.start(8080)
        /*
        IdlingRegistry.getInstance().register(
            OkHttp3IdlingResource.create(
                "okhttp",
                okHttpClient
            )
        )
         */
        IdlingRegistry.getInstance().register(okHttp3IdlingResource)
    }

    @Test
    fun mainFragmentTest(){

        /*
        val a = MockResponseFileReader("post_success.json").content
        println(a)
         */

        // Prepare Mock Web Server
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(MockResponseFileReader("post_success.json").content)
            }
        }

        val navController = mockk<NavController>(relaxed = true)

        // val action = MainFragmentDirections.actionMainFragmentToDetailFragment()
        // every { navController.navigate(action) } just Runs

        val scenario = launchFragmentInHiltContainer<MainFragment> {
            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(this.requireView(), navController)
                }
            }
        }

        onView(withId(R.id.rv_posts)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PostViewHolder>(
                0,
                click()
            )
        )

        val item = PostUiModel(
            1,
            1,
            title = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
            body = "quia et suscipit\n" +
                    "suscipit recusandae consequuntur expedita et cum\n" +
                    "reprehenderit molestiae ut ut quas totam\n" +
                    "nostrum rerum est autem sunt rem eveniet architecto"
        )

        verify { navController.navigate(MainFragmentDirections.actionMainFragmentToDetailFragment().setPost(item)) }
        // verify(exactly = 0) { navController.popBackStack() }
    }

    /**
     * Navigation Test Sample
     */
    @Test
    fun mainFragmentTest2(){

        /*
        val a = MockResponseFileReader("post_success.json").content
        println(a)
         */

        // Prepare Mock Web Server
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(MockResponseFileReader("post_success.json").content)
            }
        }

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

        onView(withId(R.id.rv_posts)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PostViewHolder>(
                0,
                click()
            )
        )

        val item = PostUiModel(
            1,
            1,
            title = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
            body = "quia et suscipit\n" +
                    "suscipit recusandae consequuntur expedita et cum\n" +
                    "reprehenderit molestiae ut ut quas totam\n" +
                    "nostrum rerum est autem sunt rem eveniet architecto"
        )


        // Get the arguments from the last destination on the back stack
        val currentDestinationArgs = navController.backStack.last().arguments
        val postArg = currentDestinationArgs?.getParcelable<PostUiModel>("post")

        // Assertions
        Truth.assertThat(navController.currentDestination?.id).isEqualTo(R.id.detailFragment)
        Truth.assertThat(postArg?.id).isEqualTo(item.id)
    }

    @After
    fun tearDown() {
        mockWebServer.close()
        IdlingRegistry.getInstance().unregister(okHttp3IdlingResource)
    }

}
 */