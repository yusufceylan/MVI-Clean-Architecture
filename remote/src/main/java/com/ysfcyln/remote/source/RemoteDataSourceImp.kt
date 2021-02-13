package com.ysfcyln.remote.source

import com.ysfcyln.common.Mapper
import com.ysfcyln.data.model.CommentDataModel
import com.ysfcyln.data.model.PostDataModel
import com.ysfcyln.data.repository.RemoteDataSource
import com.ysfcyln.remote.api.ApiService
import com.ysfcyln.remote.model.CommentNetworkModel
import com.ysfcyln.remote.model.PostNetworkModel
import javax.inject.Inject

/**
 * Implementation of [RemoteDataSource] class
 */
class RemoteDataSourceImp @Inject constructor(
    private val apiService : ApiService,
    private val postMapper : Mapper<PostNetworkModel, PostDataModel>,
    private val commentMapper : Mapper<CommentNetworkModel, CommentDataModel>
    ) : RemoteDataSource {

    override suspend fun getPosts(): List<PostDataModel> {
        val networkData = apiService.getPosts()
        return postMapper.fromList(networkData)
    }

    override suspend fun getPostComments(postId: Int): List<CommentDataModel> {
        val networkData = apiService.getPostComments(postId = postId)
        return commentMapper.fromList(networkData)
    }
}