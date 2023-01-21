package com.app.signal.data.json_serializer.factory

import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

internal class SerializerResponseBodyConverter(
    private val type: Type,
    private val json: Json,
) : Converter<ResponseBody, Any> {
    override fun convert(value: ResponseBody): Any {
        return json.fromResponseBody(type, value)
    }
}