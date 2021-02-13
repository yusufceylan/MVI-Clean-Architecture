package com.ysfcyln.remote.api

import com.ysfcyln.remote.model.PostNetworkModel
import retrofit2.http.GET

/**
 * The main services that handles all endpoint processes
 */
interface ApiService {

    /**
     * Get post list
     */
    @GET("posts")
    suspend fun getPosts() : List<PostNetworkModel>

}