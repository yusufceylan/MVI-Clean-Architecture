package com.ysfcyln.local.utils

import com.ysfcyln.local.model.PostLocalModel

class TestData {

    companion object {
        fun generatePosts() : List<PostLocalModel> {
            val item1 = PostLocalModel(1, 1, "title 1", "test body 1")
            val item2 = PostLocalModel(2, 2, "title 2", "test body 2")
            val item3 = PostLocalModel(3, 3, "title 3", "test body 3")
            return listOf(item1, item2, item3)
        }

        fun generatePostItem() : PostLocalModel {
            return PostLocalModel(1, 1, "test title","test body")
        }
    }


}