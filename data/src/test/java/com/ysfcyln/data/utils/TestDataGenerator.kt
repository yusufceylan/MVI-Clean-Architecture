package com.ysfcyln.data.utils

import com.ysfcyln.data.model.CommentDataModel
import com.ysfcyln.data.model.PostDataModel

class TestDataGenerator {

    companion object {
        fun generatePosts() : List<PostDataModel> {
            val item1 = PostDataModel(1, 1, "title 1", "test body 1")
            val item2 = PostDataModel(1, 2, "title 2", "test body 2")
            val item3 = PostDataModel(1, 3, "title 3", "test body 3")
            return listOf(item1, item2, item3)
        }

        fun generatePostComments() : List<CommentDataModel> {
            val item1 = CommentDataModel(1,1, "test name 1", "test mail 1", "test body 1")
            val item2 = CommentDataModel(1,2, "test name 2", "test mail 2", "test body 2")
            val item3 = CommentDataModel(1,3, "test name 3", "test mail 3", "test body 3")
            return listOf(item1, item2, item3)
        }
    }

}