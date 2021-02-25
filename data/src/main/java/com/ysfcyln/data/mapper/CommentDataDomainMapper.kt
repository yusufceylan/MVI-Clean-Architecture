package com.ysfcyln.data.mapper

import com.ysfcyln.common.Mapper
import com.ysfcyln.data.model.CommentDataModel
import com.ysfcyln.domain.entity.CommentEntityModel
import javax.inject.Inject

/**
 * Mapper class for convert [CommentDataModel] to [CommentEntityModel] and vice versa
 */
class CommentDataDomainMapper @Inject constructor() : Mapper<CommentDataModel, CommentEntityModel> {

    override fun from(i: CommentDataModel?): CommentEntityModel {
        return CommentEntityModel(
            postId = i?.postId,
            id = i?.id,
            name = i?.name,
            email = i?.email,
            body = i?.body
        )
    }

    override fun to(o: CommentEntityModel?): CommentDataModel {
        return CommentDataModel(
            postId = o?.postId,
            id = o?.id,
            name = o?.name,
            email = o?.email,
            body = o?.body
        )
    }
}