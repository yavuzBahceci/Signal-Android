package com.app.signal.di.core

import com.app.signal.data.rest.injector.PhotoApiInjector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideHeaderInjector(): PhotoApiInjector {
        return PhotoApiInjector()
    }

    @Provides
    fun provideRestHttpClient(
        photoApiInjector: PhotoApiInjector
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(photoApiInjector)
            .addInterceptor(HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

}