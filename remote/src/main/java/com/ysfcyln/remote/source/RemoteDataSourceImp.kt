package com.ysfcyln.remote.source

import com.ysfcyln.common.Mapper
import com.ysfcyln.data.model.PostDataModel
import com.ysfcyln.data.repository.RemoteDataSource
import com.ysfcyln.remote.api.ApiService
import com.ysfcyln.remote.model.PostNetworkModel
import javax.inject.Inject

/**
 * Implementation of [RemoteDataSource] class
 */
class RemoteDataSourceImp @Inject constructor(
    private val apiService : ApiService,
    private val postMapper : Mapper<PostNetworkModel, PostDataModel>
    ) : RemoteDataSource {

    override suspend fun getPosts(): List<PostDataModel> {
        val networkData = apiService.getPosts()
        return postMapper.fromList(networkData)
    }
}