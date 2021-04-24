package com.ysfcyln.domain.repository

import com.ysfcyln.common.Resource
import com.ysfcyln.domain.entity.CommentEntityModel
import com.ysfcyln.domain.entity.PostEntityModel
import kotlinx.coroutines.flow.Flow

/**
 * Methods of Repository
 */
interface Repository {

    suspend fun getPosts() : Flow<Resource<List<PostEntityModel>>>

    suspend fun getPostComments(postId : Int) : Flow<Resource<List<CommentEntityModel>>>

}