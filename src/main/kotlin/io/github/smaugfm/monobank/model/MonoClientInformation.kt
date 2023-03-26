package io.github.smaugfm.monobank.model

import kotlinx.serialization.Serializable

/**
 * Інформація про клієнта та перелік його рахунків і банок.
 */
@Serializable
data class MonoClientInformation(
    /**
     * Ідентифікатор клієнта (збігається з id для send.monobank.ua)
     */
    val clientId: String,
    /**
     * Ім'я клієнта
     */
    val name: String,
    /**
     * URL для надсилання подій по зміні балансу рахунку
     */
    val webHookUrl: String,
    /**
     * Перелік доступних рахунків
     */
    val accounts: List<MonoAccount> = listOf(),
    /**
     * Перелік прав, які надає сервіс (1 літера на 1 permission).
     */
    val permissions: String,
    /**
     * Перелік банок
     */
    val jars: List<MonoJar> = listOf()
)
