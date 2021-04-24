package com.ysfcyln.remote.utils

import com.ysfcyln.remote.model.CommentNetworkModel
import com.ysfcyln.remote.model.PostNetworkModel

/**
 * Dummy data generator for tests
 */
class TestDataGenerator {

    companion object {
        fun generatePosts() : List<PostNetworkModel> {
            val item1 = PostNetworkModel(1, 1, "title 1", "test body 1")
            val item2 = PostNetworkModel(1, 2, "title 2", "test body 2")
            val item3 = PostNetworkModel(1, 3, "title 3", "test body 3")
            return listOf(item1, item2, item3)
        }

        fun generatePostComments() : List<CommentNetworkModel> {
            val item1 = CommentNetworkModel(1,1, "test name 1", "test mail 1", "test body 1")
            val item2 = CommentNetworkModel(1,2, "test name 2", "test mail 2", "test body 2")
            val item3 = CommentNetworkModel(1,3, "test name 3", "test mail 3", "test body 3")
            return listOf(item1, item2, item3)
        }
    }

}