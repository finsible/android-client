package com.itsjeel01.finsiblefrontend.data.serializer

import com.itsjeel01.finsiblefrontend.common.logging.Logger
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
                val primitive = element as? JsonPrimitive
                
                if (primitive == null) {
                    Logger.Network.w("Balance field is null or not a primitive, defaulting to 0.0")
                    return "0.0"
                }
                
                // First check if it's already a string to avoid unnecessary parsing
                if (primitive.isString) {
                    return primitive.content
                }
                
                // Otherwise, try to parse as a number
                primitive.doubleOrNull?.toString() ?: run {
                    Logger.Network.w("Balance field could not be parsed as number: ${primitive.content}, defaulting to 0.0")
                    "0.0"
                }
            }
            else -> decoder.decodeString()
        }
    }
}
