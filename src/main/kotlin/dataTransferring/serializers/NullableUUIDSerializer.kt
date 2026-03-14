package com.dataTransferring.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

class NullableUUIDSerializer : KSerializer<UUID?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: UUID?) {
        if (value == null) {
            encoder.encodeNull()
        }
        else {
            encoder.encodeString(value.toString())
        }
    }

    override fun deserialize(decoder: Decoder): UUID? {
        val value = decoder.decodeString()
        return  if (value.isBlank()) null
                else UUID.fromString(value)
    }

}