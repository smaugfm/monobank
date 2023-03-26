@file:UseSerializers(CurrencyAsIntSerializer::class, InstantAsLongSerializer::class)

package io.github.smaugfm.monobank.model

import io.github.smaugfm.monobank.serializer.CurrencyAsIntSerializer
import io.github.smaugfm.monobank.serializer.InstantAsLongSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.Currency

/**
 * Базова інформація про курс валют
 */
@Serializable
data class MonoCurrencyInfo(
    /**
     * Перша валюта
     */
    val currencyCodeA: Currency,
    /**
     * Перша валюта
     */
    val currencyCodeB: Currency,
    /**
     * Момент часу на який отримано цей курс валюти
     */
    val date: Instant,
    /**
     * Курс продажу банком першої валюти за другу
     */
    val rateSell: Double? = null,
    /**
     * Курс купівлі банком першої валюти за другу
     */
    val rateBuy: Double? = null,
    /**
     * Курс кросс-обміну
     */
    val rateCross: Double? = null
)
