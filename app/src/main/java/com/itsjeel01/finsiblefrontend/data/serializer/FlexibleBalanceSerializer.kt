package com.itsjeel01.finsiblefrontend.data.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull

/**
 * Serializer that flexibly handles balance as either String or Double from the API.
 *
 * This provides backward compatibility in case the backend sends balance as a numeric value
 * instead of a string, while preferring string format for precision.
 */
object FlexibleBalanceSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("FlexibleBalance", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }

    override fun deserialize(decoder: Decoder): String {
        return when (decoder) {
            is JsonDecoder -> {
                val element = decoder.decodeJsonElement()
                when (val primitive = element as? JsonPrimitive) {
                    null -> "0.0"
                    else -> primitive.doubleOrNull?.toString() ?: primitive.content
                }
            }
            else -> decoder.decodeString()
        }
    }
}
