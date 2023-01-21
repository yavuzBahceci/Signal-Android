package com.app.signal.data.json_serializer.factory

import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import retrofit2.Converter
import java.lang.reflect.Type

internal class SerializerRequestBodyConverter(
    private val type: Type,
    private val json: Json
) : Converter<Any, RequestBody> {
    override fun convert(value: Any): RequestBody {
        return json.toRequestBody(type, value)
    }
}