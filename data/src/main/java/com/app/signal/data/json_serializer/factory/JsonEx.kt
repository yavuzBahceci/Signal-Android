@file:OptIn(ExperimentalSerializationApi::class)

package com.app.signal.data.json_serializer.factory

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.lang.reflect.Type

private val Json.mediaType get() = "application/json; charset=UTF-8".toMediaType()

fun Json.toRequestBody(type: Type, value: Any): RequestBody {
    val serializer = serializersModule.serializer(type)
    val string = encodeToString(serializer, value)
    return string.toRequestBody(mediaType)
}

fun Json.fromResponseBody(type: Type, body: ResponseBody): Any {
    val serializer = serializersModule.serializer(type)
    return decodeFromString(serializer, body.string())
}