package com.app.signal.di.core

import com.app.signal.data.json_serializer.UriSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KotlinSerializerModule {

    private val module = SerializersModule {
        contextual(UriSerializer)
    }

    @Provides
    @Singleton
    fun provideJsonSerializer(): Json {
        return Json {
            isLenient = true
            ignoreUnknownKeys = true
            coerceInputValues = true
            serializersModule = module
        }
    }
}