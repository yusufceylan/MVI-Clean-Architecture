package com.ysfcyln.domain.usecase

import com.ysfcyln.common.Resource
import com.ysfcyln.domain.entity.CommentEntityModel
import com.ysfcyln.domain.qualifiers.IoDispatcher
import com.ysfcyln.domain.repository.Repository
import com.ysfcyln.domain.usecase.core.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Use Case class for get comments of post
 */
class GetPostCommentsUseCase @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseUseCase<List<CommentEntityModel>, Int>() {

    override suspend fun buildRequest(params: Int?): Flow<Resource<List<CommentEntityModel>>> {
        if (params == null) {
            return flow {
                emit(Resource.Error(Exception("PostId can not be null")))
            }.flowOn(dispatcher)
        }
        return repository.getPostComments(postId = params).flowOn(dispatcher)
    }
}