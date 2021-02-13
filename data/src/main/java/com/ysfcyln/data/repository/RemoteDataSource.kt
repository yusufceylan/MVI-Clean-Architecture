package com.ysfcyln.data.repository

import com.ysfcyln.data.model.CommentDataModel
import com.ysfcyln.data.model.PostDataModel

/**
 * Methods of Remote Data Source
 */
interface RemoteDataSource {

    suspend fun getPosts() : List<PostDataModel>

    suspend fun getPostComments(postId : Int) : List<CommentDataModel>

}