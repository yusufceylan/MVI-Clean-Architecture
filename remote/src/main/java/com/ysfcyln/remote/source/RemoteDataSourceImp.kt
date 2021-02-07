package com.ysfcyln.remote.source

import com.ysfcyln.data.repository.RemoteDataSource
import com.ysfcyln.remote.api.ApiService
import javax.inject.Inject

/**
 * Implementation of [RemoteDataSource] class
 */
class RemoteDataSourceImp @Inject constructor(private val apiService : ApiService) : RemoteDataSource {
}