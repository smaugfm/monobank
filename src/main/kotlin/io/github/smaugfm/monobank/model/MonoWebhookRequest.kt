package io.github.smaugfm.monobank.model

import kotlinx.serialization.Serializable

@Serializable
data class MonoWebhookRequest(
    val webHookUrl: String
)
