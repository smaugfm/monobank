@file:UseSerializers(CurrencyAsIntSerializer::class, InstantAsLongSerializer::class)

package io.github.smaugfm.monobank.model

import io.github.smaugfm.monobank.serializer.CurrencyAsIntSerializer
import io.github.smaugfm.monobank.serializer.InstantAsLongSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.Currency

@Serializable
data class MonoStatementItem(
    val id: String,
    val time: Instant,
    val description: String,
    val mcc: Int,
    val originalMcc: Int,
    val hold: Boolean,
    val amount: Long,
    val operationAmount: Long,
    val currencyCode: Currency,
    val commissionRate: Long,
    val cashbackAmount: Long,
    val balance: Long,
    val comment: String = "",
    val receiptId: String,
    val invoiceId: String,
    val counterEdrpou: String,
    val counterIban: String,
    val counterName: String
)
