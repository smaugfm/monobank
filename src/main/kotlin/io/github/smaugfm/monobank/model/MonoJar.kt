@file:UseSerializers(CurrencyAsIntSerializer::class)

package io.github.smaugfm.monobank.model

import io.github.smaugfm.monobank.serializer.CurrencyAsIntSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.Currency

/**
 * Інформація про банку клієнта
 */
@Serializable
data class MonoJar(
    /**
     * Ідентифікатор банки
     */
    val id: String,
    /**
     * Ідентифікатор для сервісу [https://send.monobank.ua/{sendId}](https://send.monobank.ua/{sendId})
     */
    val sendId: String,
    /**
     * Назва банки
     */
    val title: String,
    /**
     * Опис банки
     */
    val description: String? = null,
    /**
     * Валюта банки
     */
    val currencyCode: Currency,
    /**
     * Баланс банки в мінімальних одиницях валюти (копійках, центах)
     */
    val balance: Long,
    /**
     * Цільова сума для накопичення в банці в мінімальних одиницях валюти (копійках, центах)
     */
    val goal: Long?
)
