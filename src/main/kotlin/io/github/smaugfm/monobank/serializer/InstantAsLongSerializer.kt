package io.github.smaugfm.monobank.serializer

import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object InstantAsLongSerializer : KSerializer<Instant> {
    override val descriptor = PrimitiveSerialDescriptor(
        this::class.qualifiedName!!,
        PrimitiveKind.LONG
    )

    override fun deserialize(decoder: Decoder) = Instant.fromEpochSeconds(decoder.decodeLong())

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeLong(value.epochSeconds)
    }
}
