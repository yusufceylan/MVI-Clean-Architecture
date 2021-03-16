package com.ysfcyln.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostUiModel(
    val userId: Int?,
    val id: Int?,
    val title: String?,
    val body: String?
) : Parcelable