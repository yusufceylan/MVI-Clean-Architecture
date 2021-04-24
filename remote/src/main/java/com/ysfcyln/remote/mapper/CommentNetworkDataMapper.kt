package com.ysfcyln.remote.mapper

import com.ysfcyln.common.Mapper
import com.ysfcyln.data.model.CommentDataModel
import com.ysfcyln.remote.model.CommentNetworkModel
import javax.inject.Inject

/**
 * Mapper class for convert [CommentNetworkModel] to [CommentDataModel] and vice versa
 */
class CommentNetworkDataMapper @Inject constructor() : Mapper<CommentNetworkModel, CommentDataModel> {

    override fun from(i: CommentNetworkModel?): CommentDataModel {
        return CommentDataModel(
            postId = i?.postId,
            id = i?.id,
            name = i?.name,
            email = i?.email,
            body = i?.body
        )
    }

    override fun to(o: CommentDataModel?): CommentNetworkModel {
        return CommentNetworkModel(
            postId = o?.postId,
            id = o?.id,
            name = o?.name,
            email = o?.email,
            body = o?.body
        )
    }
}