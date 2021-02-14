package com.ysfcyln.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostLocalModel(
    @PrimaryKey
    val id: Int?,
    val userId: Int?,
    val title: String?,
    val body: String?
)