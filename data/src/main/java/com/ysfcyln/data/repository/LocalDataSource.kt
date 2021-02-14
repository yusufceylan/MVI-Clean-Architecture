package com.ysfcyln.data.repository

import com.ysfcyln.data.model.PostDataModel

/**
 * Methods of Local Data Source
 */
interface LocalDataSource {

    suspend fun addPostItem(post : PostDataModel) : Long

    suspend fun getPostItem(id: Int): PostDataModel

    suspend fun addPostItems(posts: List<PostDataModel>)

    suspend fun getPostItems(): List<PostDataModel>

    suspend fun updatePostItem(post: PostDataModel): Int

    suspend fun deletePostItemById(id : Int) : Int

    suspend fun deletePostItem(post : PostDataModel) : Int

    suspend fun clearCachedPostItems(): Int
}