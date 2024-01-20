package org.unizd.rma.kovacevic.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.unizd.rma.kovacevic.data.repository.LocationRepositoryImpl
import org.unizd.rma.kovacevic.domain.repository.Repository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRepository(repository: LocationRepositoryImpl):Repository
}