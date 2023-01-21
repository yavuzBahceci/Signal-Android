package com.app.signal.di.data

import com.app.signal.data.repository.photo.PhotoRepositoryImpl
import com.app.signal.data.repository.photo.store.PhotoLocalStore
import com.app.signal.data.repository.photo.store.PhotoRemoteStore
import com.app.signal.data.repository.photo.store.PhotoRetrofitStore
import com.app.signal.data.repository.photo.store.PhotoRoomStore
import com.app.signal.domain.repository.PhotoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PhotoDataModule {
    @Binds
    abstract fun bindRemote(impl: PhotoRetrofitStore): PhotoRemoteStore

    @Binds
    abstract fun bindLocal(impl: PhotoRoomStore): PhotoLocalStore

    @Binds
    @Singleton
    abstract fun bindRepository(impl: PhotoRepositoryImpl): PhotoRepository
}