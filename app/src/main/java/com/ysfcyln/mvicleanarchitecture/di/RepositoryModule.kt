package com.ysfcyln.mvicleanarchitecture.di

import com.ysfcyln.data.repository.LocalDataSource
import com.ysfcyln.data.repository.RemoteDataSource
import com.ysfcyln.data.repository.RepositoryImp
import com.ysfcyln.domain.repository.Repository
import com.ysfcyln.local.source.LocalDataSourceImp
import com.ysfcyln.remote.source.RemoteDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Module that holds Repository classes
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideLocalDataSource(localDataSourceImpl: LocalDataSourceImp): LocalDataSource

    @Binds
    abstract fun provideRemoteDataSource(remoteDataSourceImp: RemoteDataSourceImp): RemoteDataSource

    @Binds
    @ViewModelScoped
    abstract fun provideRepository(repository : RepositoryImp) : Repository

}