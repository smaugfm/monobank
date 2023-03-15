@file:UseSerializers(CurrencyAsIntSerializer::class)

package io.github.smaugfm.monobank.model

import io.github.smaugfm.monobank.serializer.CurrencyAsIntSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.Currency

@Serializable
data class MonoAccount(
    val id: String,
    val sendId: String,
    val balance: Long,
    val creditLimit: Long,
    val type: String,
    val currencyCode: Currency,
    val cashbackType: MonoCashbackType? = MonoCashbackType.None,
    val iban: String,
    val maskedPan: List<String> = listOf()
)
