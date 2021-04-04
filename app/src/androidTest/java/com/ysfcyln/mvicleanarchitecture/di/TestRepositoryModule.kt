package com.ysfcyln.mvicleanarchitecture.di

import com.ysfcyln.data.repository.RepositoryImp
import com.ysfcyln.domain.repository.Repository
import com.ysfcyln.mvicleanarchitecture.utils.FakeRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Repository binding to use in tests.
 *
 * Hilt will inject a [FakeRepositoryImp] instead of a [RepositoryImp].
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class TestRepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun provideRepository(repository : FakeRepositoryImp) : Repository

}