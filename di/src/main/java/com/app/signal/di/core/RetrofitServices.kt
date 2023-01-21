package com.app.signal.di.core

import com.app.signal.data.BuildConfig
import com.app.signal.data.json_serializer.factory.SerializerConverterFactory
import com.app.signal.data.rest.api.PhotoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitServices {


    @Provides
    @Singleton
    fun provideRetrofit(
        json: Json,
        client: OkHttpClient,
    ): Retrofit {
        return resolveRetrofitInstance(
            json,
            client
                .newBuilder()
                .build()
        )
    }

    private fun resolveRetrofitInstance(
        json: Json,
        httpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(SerializerConverterFactory(json))
            .client(httpClient)
            .build()
    }

    @Provides
    fun providePhotoApi(retrofit: Retrofit): PhotoApi {
        return retrofit.create()
    }
}