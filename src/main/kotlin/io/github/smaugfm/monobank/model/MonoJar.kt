@file:UseSerializers(CurrencyAsIntSerializer::class)

package io.github.smaugfm.monobank.model

import io.github.smaugfm.monobank.serializer.CurrencyAsIntSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.Currency

@Serializable
data class MonoJar(
    val id: String,
    val sendId: String,
    val title: String,
    val description: String,
    val currencyCode: Currency,
    val balance: Long,
    val goal: Long
)
