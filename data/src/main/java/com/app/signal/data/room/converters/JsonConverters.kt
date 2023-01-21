package com.app.signal.data.room.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.app.signal.domain.model.photo.Image
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@ProvidedTypeConverter
data class JsonConverters @Inject constructor(private val jsonAdapter: Json) {
    @TypeConverter
    fun fromImageDto(dto: Image): String {
        return jsonAdapter.encodeToString(dto)
    }

    @TypeConverter
    fun toImageDto(json: String): Image {
        return jsonAdapter.decodeFromString(json)
    }
}
