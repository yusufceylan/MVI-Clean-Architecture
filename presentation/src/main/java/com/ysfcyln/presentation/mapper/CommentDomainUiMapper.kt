package com.ysfcyln.presentation.mapper

import com.ysfcyln.common.Mapper
import com.ysfcyln.domain.entity.CommentEntityModel
import com.ysfcyln.presentation.model.CommentUiModel
import javax.inject.Inject

/**
 * Mapper class for convert [CommentEntityModel] to [CommentUiModel] and vice versa
 */
class CommentDomainUiMapper @Inject constructor() : Mapper<CommentEntityModel, CommentUiModel> {

    override fun from(i: CommentEntityModel?): CommentUiModel {
        return CommentUiModel(
            postId = i?.postId,
            id = i?.id,
            name = i?.name,
            email = i?.email,
            body = i?.body
        )
    }

    override fun to(o: CommentUiModel?): CommentEntityModel {
        return CommentEntityModel(
            postId = o?.postId,
            id = o?.id,
            name = o?.name,
            email = o?.email,
            body = o?.body
        )
    }
}