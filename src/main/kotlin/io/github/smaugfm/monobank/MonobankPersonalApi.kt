package io.github.smaugfm.monobank

import io.github.resilience4j.ratelimiter.RateLimiterConfig
import io.github.smaugfm.monobank.model.MonoClientInformation
import io.github.smaugfm.monobank.model.MonoStatementItem
import io.github.smaugfm.monobank.model.MonoWebhookRequest
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.serializer
import reactor.core.publisher.Mono
import reactor.netty.resources.ConnectionProvider

/**
 * Personal section of [Monobank](https://api.monobank.ua/docs/) API.
 *
 * @param token Personal API token
 * @param jsonBuilderAction action to customize internal [kotlinx.serialization.json.Json] instance
 * @param reactorNettyConnectionProvider custom [ConnectionProvider]
 * for internal reactor-netty [reactor.netty.http.client.HttpClient]
 */
@ExperimentalSerializationApi
class MonobankPersonalApi internal constructor(
    private val token: String,
    baseUrl: String,
    port: Int,
    jsonBuilderAction: JsonBuilder.() -> Unit = {},
    rateLimiterConfig: RateLimiterConfig = defaultRateLimiterConfig(),
    reactorNettyConnectionProvider: ConnectionProvider? = null
) : MonobankApiBase(
    baseUrl,
    port,
    jsonBuilderAction,
    rateLimiterConfig,
    reactorNettyConnectionProvider
) {

    constructor(
        token: String,
        jsonBuilderAction: JsonBuilder.() -> Unit = {},
        rateLimiterConfig: RateLimiterConfig = defaultRateLimiterConfig(),
        reactorNettyConnectionProvider: ConnectionProvider? = null
    ) : this(
        token,
        BASE_URL,
        PORT,
        rateLimiterConfig = rateLimiterConfig,
        jsonBuilderAction = jsonBuilderAction,
        reactorNettyConnectionProvider = reactorNettyConnectionProvider
    )

    init {
        initRateLimiters(
            this::getClientStatements,
            this::getClientInformation,
            this::setClientWebhook
        )
    }

    /**
     * Отримання інформації про клієнта та переліку його рахунків і банок.
     * Обмеження на використання функції не частіше ніж 1 раз у 60 секунд.
     */
    fun getClientInformation(): Mono<MonoClientInformation> =
        requestExecutor.executeGet<MonoClientInformation>(
            buildUri("/personal/client-info"),
            json.serializersModule.serializer(),
            token
        ).withRateLimiter(this::getClientInformation)

    /**
     * Встановлення [url] користувача на який будуть надходити нові транзакції [MonoStatementItem]:
     *
     *  - Для підтвердження коректності наданої адреси, на неї надсилається GET-запит.
     *    Сервер має відповісти строго HTTP статус-кодом 200, і ніяким іншим.
     *    Якщо валідацію пройдено, на задану адресу починають надсилатися POST запити з подіями.
     *  - Якщо сервіс користувача не відповість протягом 5с на команду, сервіс повторить спробу
     *    ще через 60 та 600 секунд. Якщо на третю спробу відповідь отримана не буде,
     *    функція буде вимкнута. Відповідь сервера має строго містити HTTP статус-код 200.
     *
     * @param url URL на який будуть надходити нові транзакції
     */
    fun setClientWebhook(url: String): Mono<Void> =
        requestExecutor.executePost<MonoWebhookRequest, Void>(
            buildUri("/personal/webhook"),
            MonoWebhookRequest(url),
            json.serializersModule.serializer(),
            null,
            token
        ).withRateLimiter(this::setClientWebhook)

    /**
     * Отримання виписки за час від [from] до [to].
     * Максимальний час за який можливо отримувати виписку 31 доба + 1 година (2682000 секунд)
     * Обмеження на використання функції не частіше ніж 1 раз у 60 секунд.
     *
     * Повертає 500 транзакцій з кінця, тобто від часу to до from.
     * Якщо кількість транзакцій = 500, потрібно зробити ще один запит,
     * зменшивши час to до часу останнього платежу, з відповіді.
     * Якщо знову кількість транзакцій = 500, то виконуєте запити до того часу,
     * поки кількість транзакцій не буде < 500.
     * Відповідно, якщо кількість транзакцій < 500, то вже отримано всі платежі за вказаний період.
     *
     * @param account Ідентифікатор рахунку або банки з переліків Statement list або 0 - дефолтний рахунок.
     * @param from Початок часу виписки.
     * @param to Останній час виписки (якщо відсутній, буде використовуватись поточний час).
     */
    fun getClientStatements(
        account: String,
        from: Instant,
        to: Instant? = null
    ): Mono<List<MonoStatementItem>> {
        var path = "/personal/statement/$account/${from.epochSeconds}"
        if (to != null) {
            path += "/${to.epochSeconds}"
        }
        return requestExecutor.executeGet<List<MonoStatementItem>>(
            buildUri(path),
            json.serializersModule.serializer(),
            token
        ).withRateLimiter(this::getClientStatements)
    }
}
