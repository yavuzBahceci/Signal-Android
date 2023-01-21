package com.app.signal.di.core

import android.content.Context
import com.app.signal.data.room.AppDatabase
import com.app.signal.data.room.converters.JsonConverters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    fun provideJsonConverters(json: kotlinx.serialization.json.Json): JsonConverters {
        return JsonConverters(json)
    }

    @Provides
    @Singleton
    fun provideDataBase(
        @ApplicationContext ctx: Context,
        jsonConverters: JsonConverters
    ): AppDatabase {
        return AppDatabase.setup(ctx, jsonConverters)
    }
}