package io.github.smaugfm.monobank.model

import kotlinx.serialization.Serializable

@Serializable
data class MonoClientInformation(
    val clientId: String,
    val name: String,
    val webHookUrl: String,
    val accounts: List<MonoAccount> = listOf(),
    val permissions: String,
    val jars: List<MonoJar> = listOf()
)
