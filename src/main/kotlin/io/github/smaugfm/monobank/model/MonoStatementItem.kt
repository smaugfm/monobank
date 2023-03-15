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
    val description: String? = null,
    val mcc: Int,
    val originalMcc: Int,
    val hold: Boolean,
    val amount: Long,
    val operationAmount: Long,
    val currencyCode: Currency,
    val commissionRate: Long,
    val cashbackAmount: Long,
    val balance: Long,
    val comment: String? = null,
    val receiptId: String? = null,
    val invoiceId: String? = null,
    val counterEdrpou: String? = null,
    val counterIban: String? = null,
    val counterName: String? = null
)
