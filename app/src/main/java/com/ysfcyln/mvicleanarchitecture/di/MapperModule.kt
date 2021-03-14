package com.ysfcyln.mvicleanarchitecture.di

import com.ysfcyln.common.Mapper
import com.ysfcyln.data.mapper.CommentDataDomainMapper
import com.ysfcyln.data.mapper.PostDataDomainMapper
import com.ysfcyln.data.model.CommentDataModel
import com.ysfcyln.data.model.PostDataModel
import com.ysfcyln.domain.entity.CommentEntityModel
import com.ysfcyln.domain.entity.PostEntityModel
import com.ysfcyln.local.mapper.PostLocalDataMapper
import com.ysfcyln.local.model.PostLocalModel
import com.ysfcyln.presentation.mapper.CommentDomainUiMapper
import com.ysfcyln.presentation.mapper.PostDomainUiMapper
import com.ysfcyln.presentation.model.CommentUiModel
import com.ysfcyln.presentation.model.PostUiModel
import com.ysfcyln.remote.mapper.CommentNetworkDataMapper
import com.ysfcyln.remote.mapper.PostNetworkDataMapper
import com.ysfcyln.remote.model.CommentNetworkModel
import com.ysfcyln.remote.model.PostNetworkModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Module that holds Mappers
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class MapperModule {

    //region Remote Mappers
    @Binds
    abstract fun bindsPostNetworkDataMapper(mapper: PostNetworkDataMapper): Mapper<PostNetworkModel, PostDataModel>

    @Binds
    abstract fun bindsCommentNetworkDataMapper(mapper : CommentNetworkDataMapper) : Mapper<CommentNetworkModel, CommentDataModel>
    //endregion

    //region Locale Mappers
    @Binds
    abstract fun bindsPostLocalDataMapper(mapper : PostLocalDataMapper) : Mapper<PostLocalModel, PostDataModel>
    //endregion

    //region Data Mappers
    @Binds
    abstract fun bindsPostDataDomainMapper(mapper : PostDataDomainMapper) : Mapper<PostDataModel, PostEntityModel>

    @Binds
    abstract fun bindsCommentDataDomainMapper(mapper : CommentDataDomainMapper) : Mapper<CommentDataModel, CommentEntityModel>
    //endregion

    //region Presentation Mappers
    @Binds
    abstract fun bindsPostDomainUiMapper(mapper : PostDomainUiMapper) : Mapper<PostEntityModel, PostUiModel>

    @Binds
    abstract fun bindsCommentDomainUiMapper(mapper : CommentDomainUiMapper) : Mapper<CommentEntityModel, CommentUiModel>
    //endregion

}