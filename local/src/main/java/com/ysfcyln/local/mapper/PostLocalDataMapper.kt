package com.ysfcyln.local.mapper

import com.ysfcyln.common.Mapper
import com.ysfcyln.data.model.PostDataModel
import com.ysfcyln.local.model.PostLocalModel
import javax.inject.Inject

/**
 * Mapper class for convert [PostLocalModel] to [PostDataModel] and vice versa
 */
class PostLocalDataMapper @Inject constructor() : Mapper<PostLocalModel, PostDataModel> {

    override fun from(i: PostLocalModel?): PostDataModel {
        return PostDataModel(
            userId = i?.userId,
            id = i?.id,
            title = i?.title,
            body = i?.body
        )
    }

    override fun to(o: PostDataModel?): PostLocalModel {
        return PostLocalModel(
            userId = o?.userId,
            id = o?.id,
            title = o?.title,
            body = o?.body
        )
    }
}