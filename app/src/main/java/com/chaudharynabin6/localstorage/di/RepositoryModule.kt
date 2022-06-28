package com.chaudharynabin6.localstorage.di

import com.chaudharynabin6.localstorage.data.repository.InternalStorageRepositoryImp
import com.chaudharynabin6.localstorage.domain.repository.InternalStorageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindsInternalStorageRepository(
        internalStorageRepositoryImp: InternalStorageRepositoryImp
    ) : InternalStorageRepository

}