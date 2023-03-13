package io.github.smaugfm.monobank.model

import kotlinx.serialization.Serializable

@Serializable
data class MonoUserInfo(
    val clientId: String,
    val name: String,
    val webHookUrl: String,
    val accounts: List<MonoAccount>,
)
