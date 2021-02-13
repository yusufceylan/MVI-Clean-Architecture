package com.ysfcyln.data.repository

import com.ysfcyln.data.model.PostDataModel

/**
 * Methods of Remote Data Source
 */
interface RemoteDataSource {

    suspend fun getPosts() : List<PostDataModel>

}