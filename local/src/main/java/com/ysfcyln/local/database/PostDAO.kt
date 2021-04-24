package com.ysfcyln.local.database

import androidx.room.*
import com.ysfcyln.local.model.PostLocalModel

@Dao
interface PostDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPostItem(post : PostLocalModel) : Long

    @Query("SELECT * FROM posts WHERE id = :id")
    suspend fun getPostItem(id: Int): PostLocalModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPostItems(posts: List<PostLocalModel>) : List<Long>

    @Query("SELECT * FROM posts")
    suspend fun getPostItems(): List<PostLocalModel>

    @Update
    suspend fun updatePostItem(post: PostLocalModel): Int

    @Query("DELETE FROM posts WHERE id = :id")
    suspend fun deletePostItemById(id : Int) : Int

    @Delete
    suspend fun deletePostItem(post : PostLocalModel) : Int

    @Query("DELETE FROM posts")
    suspend fun clearCachedPostItems(): Int
}