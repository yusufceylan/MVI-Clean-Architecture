package com.ysfcyln.remote.api

import com.ysfcyln.remote.model.CommentNetworkModel
import com.ysfcyln.remote.model.PostNetworkModel
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * The main services that handles all endpoint processes
 */
interface ApiService {

    /**
     * Get post list
     */
    @GET("posts")
    suspend fun getPosts() : List<PostNetworkModel>

    /**
     * Get post's comment list
     */
    @GET("posts/{postId}/comments")
    suspend fun getPostComments(@Path("postId") postId : Int) : List<CommentNetworkModel>

}