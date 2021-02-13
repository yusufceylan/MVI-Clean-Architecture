package com.ysfcyln.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostNetworkModel(
    @SerialName("userId")
    val userId: Int?,
    @SerialName("id")
    val id: Int?,
    @SerialName("title")
    val title: String?,
    @SerialName("body")
    val body: String?
)