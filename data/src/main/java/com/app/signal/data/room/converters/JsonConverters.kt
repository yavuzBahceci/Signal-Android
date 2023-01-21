package com.app.signal.data.room.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.app.signal.data.dto.response.photo.AnyImageDto
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@ProvidedTypeConverter
data class JsonConverters @Inject constructor(private val jsonAdapter: Json) {
    @TypeConverter
    fun fromImageDto(dto: AnyImageDto): String {
        return jsonAdapter.encodeToString(dto)
    }

    @TypeConverter
    fun toImageDto(json: String): AnyImageDto {
        return jsonAdapter.decodeFromString(json)
    }
}
