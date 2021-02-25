package com.ysfcyln.data.mapper

import com.ysfcyln.common.Mapper
import com.ysfcyln.data.model.PostDataModel
import com.ysfcyln.domain.entity.PostEntityModel
import javax.inject.Inject

/**
 * Mapper class for convert [PostDataModel] to [PostEntityModel] and vice versa
 */
class PostDataDomainMapper @Inject constructor() : Mapper<PostDataModel, PostEntityModel> {

    override fun from(i: PostDataModel?): PostEntityModel {
        return PostEntityModel(
            userId = i?.userId,
            id = i?.id,
            title = i?.title,
            body = i?.body
        )
    }

    override fun to(o: PostEntityModel?): PostDataModel {
        return PostDataModel(
            userId = o?.userId,
            id = o?.id,
            title = o?.title,
            body = o?.body
        )
    }
}