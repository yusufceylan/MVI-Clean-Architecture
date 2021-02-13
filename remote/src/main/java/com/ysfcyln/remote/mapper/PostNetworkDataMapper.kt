package com.ysfcyln.remote.mapper

import com.ysfcyln.common.Mapper
import com.ysfcyln.data.model.PostDataModel
import com.ysfcyln.remote.model.PostNetworkModel
import javax.inject.Inject

/**
 * Mapper class for convert [PostNetworkModel] to [PostDataModel] and vice versa
 */
class PostNetworkDataMapper @Inject constructor() : Mapper<PostNetworkModel, PostDataModel> {

    override fun from(i: PostNetworkModel?): PostDataModel {
        return PostDataModel(
            userId = i?.userId,
            id = i?.id,
            title = i?.title,
            body = i?.body
        )
    }

    override fun to(o: PostDataModel?): PostNetworkModel {
        return PostNetworkModel(
            userId = o?.userId,
            id = o?.id,
            title = o?.title,
            body = o?.body
        )
    }
}