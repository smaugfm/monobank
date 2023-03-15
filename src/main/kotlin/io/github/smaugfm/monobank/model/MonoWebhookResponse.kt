package io.github.smaugfm.monobank.model

import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable
data class MonoWebhookResponse(
    val type: String,
    val data: MonoWebhookResponseData
)
