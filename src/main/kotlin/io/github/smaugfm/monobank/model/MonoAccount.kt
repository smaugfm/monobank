@file:UseSerializers(CurrencyAsIntSerializer::class)

package io.github.smaugfm.monobank.model

import io.github.smaugfm.monobank.serializer.CurrencyAsIntSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.Currency

/**
 * Інформація про рахунок клієнта
 */
@Serializable
data class MonoAccount(
    /**
     * Ідентифікатор рахунку
     */
    val id: String,
    /**
     * Ідентифікатор для сервісу [https://send.monobank.ua/{sendId}](https://send.monobank.ua/{sendId})
     */
    val sendId: String,
    /**
     * Баланс рахунку в мінімальних одиницях валюти (копійках, центах)
     */
    val balance: Long,
    /**
     * Кредитний ліміт
     */
    val creditLimit: Long,
    /**
     * Тип рахунку
     * Enum: ["black", "white", "platinum", "iron", "fop", "yellow", "eAid"]
     */
    val type: String,
    /**
     *  Валюта рахунку
     */
    val currencyCode: Currency,
    /**
     * Тип кешбеку який нараховується на рахунок
     */
    val cashbackType: MonoCashbackType? = MonoCashbackType.None,
    /**
     * IBAN рахунку
     */
    val iban: String,
    /**
     * Перелік замаскованних номерів карт (більше одного може бути у преміальних карт)
     */
    val maskedPan: List<String> = listOf()
)
