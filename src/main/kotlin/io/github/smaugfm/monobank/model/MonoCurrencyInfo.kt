@file:UseSerializers(CurrencyAsIntSerializer::class, InstantAsLongSerializer::class)

package io.github.smaugfm.monobank.model

import io.github.smaugfm.monobank.serializer.CurrencyAsIntSerializer
import io.github.smaugfm.monobank.serializer.InstantAsLongSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.UseSerializers
import java.util.Currency

@kotlinx.serialization.Serializable
data class MonoCurrencyInfo(
    val currencyCodeA: Currency,
    val currencyCodeB: Currency,
    val date: Instant,
    val rateSell: Double? = null,
    val rateBuy: Double? = null,
    val rateCross: Double? = null
)
