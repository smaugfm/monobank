package io.github.smaugfm.monobank.model

import kotlinx.serialization.Serializable

@Serializable
internal data class MonoWebhookRequest(
    val webHookUrl: String
)
