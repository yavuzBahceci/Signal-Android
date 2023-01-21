package com.app.signal.di.data

import com.app.signal.data.service.photo.PhotoServiceImpl
import com.app.signal.domain.service.PhotoService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class PhotoViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindService(impl: PhotoServiceImpl): PhotoService
}