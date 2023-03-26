@file:UseSerializers(CurrencyAsIntSerializer::class, InstantAsLongSerializer::class)

package io.github.smaugfm.monobank.model

import io.github.smaugfm.monobank.serializer.CurrencyAsIntSerializer
import io.github.smaugfm.monobank.serializer.InstantAsLongSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.Currency

/**
 * Опис транзакції клієнта
 */
@Serializable
data class MonoStatementItem(
    /**
     * Унікальний id транзакції
     */
    val id: String,
    /**
     * Час транзакції
     */
    val time: Instant,
    /**
     * Опис транзакції
     */
    val description: String? = null,
    /**
     * Код типу транзакції (Merchant Category Code), відповідно ISO 18245
     */
    val mcc: Int,
    /**
     * Оригінальний код типу транзакції (Merchant Category Code), відповідно ISO 18245
     */
    val originalMcc: Int,
    /**
     * Статус блокування суми (детальніше у [wiki](https://en.wikipedia.org/wiki/Authorization_hold))
     */
    val hold: Boolean,
    /**
     * Сума у валюті рахунку в мінімальних одиницях валюти (копійках, центах)
     */
    val amount: Long,
    /**
     * Сума у валюті транзакції в мінімальних одиницях валюти (копійках, центах)
     */
    val operationAmount: Long,
    /**
     * Валюта транзакції
     */
    val currencyCode: Currency,
    /**
     * Розмір комісії в мінімальних одиницях валюти (копійках, центах)
     */
    val commissionRate: Long,
    /**
     * Розмір кешбеку в мінімальних одиницях валюти (копійках, центах)
     */
    val cashbackAmount: Long,
    /**
     * Баланс рахунку після здійснення транзакції в мінімальних одиницях валюти (копійках, центах)
     */
    val balance: Long,
    /**
     * Коментар до переказу, уведений користувачем. Якщо не вказаний, буде null
     */
    val comment: String? = null,
    /**
     * Номер квитанції для check.gov.ua. Поле може бути відсутнім
     */
    val receiptId: String? = null,
    /**
     * Номер квитанції ФОПа, приходить у випадку якщо це операція із зарахуванням коштів
     */
    val invoiceId: String? = null,
    /**
     * ЄДРПОУ контрагента, присутній лише для елементів виписки рахунків ФОП
     */
    val counterEdrpou: String? = null,
    /**
     * IBAN контрагента, присутній лише для елементів виписки рахунків ФОП
     */
    val counterIban: String? = null,
    /**
     * Найменування контрагента, присутній лише для елементів виписки рахунків ФОП
     */
    val counterName: String? = null
)
