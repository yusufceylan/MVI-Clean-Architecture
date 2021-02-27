package com.ysfcyln.domain.usecase

import com.ysfcyln.common.Resource
import com.ysfcyln.domain.entity.PostEntityModel
import com.ysfcyln.domain.qualifiers.IoDispatcher
import com.ysfcyln.domain.repository.Repository
import com.ysfcyln.domain.usecase.core.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Use Case class for get post list
 */
class GetPostsUseCase @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseUseCase<List<PostEntityModel>, Nothing>() {

    override suspend fun buildRequest(params: Nothing?): Flow<Resource<List<PostEntityModel>>> {
        return repository.getPosts().flowOn(dispatcher)
    }
}