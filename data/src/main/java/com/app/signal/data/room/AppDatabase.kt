package com.app.signal.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.signal.data.room.converters.Converters
import com.app.signal.data.room.converters.JsonConverters
import com.app.signal.data.room.dao.photo.PhotoDao
import com.app.signal.data.room.entities.PhotoEntity

@Database(
    entities = [
        PhotoEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    Converters::class,
    JsonConverters::class
)
abstract class AppDatabase : RoomDatabase() {
    internal abstract val photoDao: PhotoDao

    companion object {
        fun setup(
            ctx: Context,
            jsonConverter: JsonConverters,
        ): AppDatabase {
            return Room
                .databaseBuilder(ctx, AppDatabase::class.java, "Signal-db.sqlite")
                .fallbackToDestructiveMigration()
                .addTypeConverter(jsonConverter)
                .build()
        }
    }
}