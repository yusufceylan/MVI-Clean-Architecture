package com.ysfcyln.mvicleanarchitecture.utils

import com.ysfcyln.common.Resource
import com.ysfcyln.domain.entity.CommentEntityModel
import com.ysfcyln.domain.entity.PostEntityModel
import com.ysfcyln.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Fake repository for UI testing
 */
class FakeRepositoryImp @Inject constructor() : Repository {

    override suspend fun getPosts(): Flow<Resource<List<PostEntityModel>>> {
        return flow { emit(Resource.Success(TestDataGenerator.generatePosts())) }
    }

    override suspend fun getPostComments(postId: Int): Flow<Resource<List<CommentEntityModel>>> {
        return flow { emit(Resource.Success(TestDataGenerator.generatePostComments())) }
    }
}