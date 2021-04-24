package com.ysfcyln.data.repository

import com.ysfcyln.common.Mapper
import com.ysfcyln.common.Resource
import com.ysfcyln.data.model.CommentDataModel
import com.ysfcyln.data.model.PostDataModel
import com.ysfcyln.domain.entity.CommentEntityModel
import com.ysfcyln.domain.entity.PostEntityModel
import com.ysfcyln.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Implementation class of [Repository]
 */
class RepositoryImp @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val postMapper : Mapper<PostDataModel, PostEntityModel>,
    private val commentMapper : Mapper<CommentDataModel, CommentEntityModel>
) : Repository {

    override suspend fun getPosts(): Flow<Resource<List<PostEntityModel>>> {
        return flow {
            try {
                // Get data from RemoteDataSource
                val data = remoteDataSource.getPosts()
                // Save to local
                localDataSource.addPostItems(data)
                // Emit data
                emit(Resource.Success(postMapper.fromList(data)))
            } catch (ex : Exception) {
                // If remote request fails
                try {
                    // Get data from LocalDataSource
                    val local = localDataSource.getPostItems()
                    // Emit data
                    emit(Resource.Success(postMapper.fromList(local)))
                } catch (ex1 : Exception) {
                    // Emit error
                    emit(Resource.Error(ex1))
                }
            }
        }
    }

    override suspend fun getPostComments(postId: Int): Flow<Resource<List<CommentEntityModel>>> {
        return flow {
            try {
                // Get data from RemoteDataSource
                val data = remoteDataSource.getPostComments(postId = postId)
                // Emit data
                emit(Resource.Success(commentMapper.fromList(data)))
            } catch (ex : Exception) {
                // Emit error
                emit(Resource.Error(ex))
            }
        }
    }
}