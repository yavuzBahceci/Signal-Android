package com.app.signal.di.core

import com.app.android_services.AndroidStorage
import com.app.signal.data.service.DataMediatorImpl
import com.app.signal.data.service.ErrorHandlerImpl
import com.app.signal.domain.service.AppStorage
import com.app.signal.domain.service.DataMediator
import com.app.signal.domain.service.ErrorHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServicesModule {

    @Binds
    @Singleton
    abstract fun bindDataMediator(impl: DataMediatorImpl): DataMediator

    @Binds
    @Singleton
    abstract fun bindStorage(impl: AndroidStorage): AppStorage

    @Binds
    @Singleton
    abstract fun bindErrorHandler(impl: ErrorHandlerImpl): ErrorHandler
}