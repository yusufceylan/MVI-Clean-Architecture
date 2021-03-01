package com.ysfcyln.presentation.mapper

import com.ysfcyln.common.Mapper
import com.ysfcyln.domain.entity.PostEntityModel
import com.ysfcyln.presentation.model.PostUiModel
import javax.inject.Inject

/**
 * Mapper class for convert [PostEntityModel] to [PostUiModel] and vice versa
 */
class PostDomainUiMapper @Inject constructor() : Mapper<PostEntityModel, PostUiModel> {

    override fun from(i: PostEntityModel?): PostUiModel {
        return PostUiModel(
            userId = i?.userId,
            id = i?.id,
            title = i?.title,
            body = i?.body
        )
    }

    override fun to(o: PostUiModel?): PostEntityModel {
        return PostEntityModel(
            userId = o?.userId,
            id = o?.id,
            title = o?.title,
            body = o?.body
        )
    }
}