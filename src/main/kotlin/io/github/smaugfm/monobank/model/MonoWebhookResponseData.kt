package io.github.smaugfm.monobank.model

import kotlinx.serialization.Serializable

@Serializable
data class MonoWebhookResponseData(
    val account: String,
    val statementItem: MonoStatementItem
)
