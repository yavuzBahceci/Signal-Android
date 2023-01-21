package com.app.signal.data.json_serializer

import android.net.Uri
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object UriSerializer : KSerializer<Uri> {
    override fun deserialize(decoder: Decoder): Uri {
        val value = decoder.decodeString()
        return Uri.parse(value)
    }

    override fun serialize(encoder: Encoder, value: Uri) {
        encoder.encodeString(value.toString())
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Uri", PrimitiveKind.STRING)
}