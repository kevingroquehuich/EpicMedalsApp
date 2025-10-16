package com.roque.domain.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = AnimationTypeSerializer::class)
enum class AnimationType {
    SPARKLE, CONFETTI, PULSE, SCALE_POP, FLASH, ROTATE, SHINE, BOUNCE, EXPLOSION, CROWN_BURST
}

object AnimationTypeSerializer : KSerializer<AnimationType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("AnimationType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: AnimationType) {
        encoder.encodeString(value.name.lowercase())
    }

    override fun deserialize(decoder: Decoder): AnimationType {
        val value = decoder.decodeString().lowercase()
        return AnimationType.entries.find { it.name.lowercase() == value } ?: AnimationType.PULSE
    }
}