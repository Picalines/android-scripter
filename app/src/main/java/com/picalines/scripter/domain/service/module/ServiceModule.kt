package com.picalines.scripter.domain.service.module

import com.picalines.scripter.domain.service.AccountService
import com.picalines.scripter.domain.service.StorageService
import com.picalines.scripter.domain.service.impl.AccountServiceImpl
import com.picalines.scripter.domain.service.impl.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds
    abstract fun provideStorageService(impl: StorageServiceImpl): StorageService
}