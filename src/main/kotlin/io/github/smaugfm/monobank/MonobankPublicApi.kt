package io.github.smaugfm.monobank

import io.github.resilience4j.ratelimiter.RateLimiterConfig
import io.github.smaugfm.monobank.model.MonoCurrencyInfo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.serializer
import reactor.core.publisher.Mono
import reactor.netty.resources.ConnectionProvider

/**
 * Public section of [Monobank](https://api.monobank.ua/docs/) API.
 *
 * @param jsonBuilderAction action to customize internal [kotlinx.serialization.json.Json] instance
 * @param reactorNettyConnectionProvider custom [ConnectionProvider]
 * for internal reactor-netty [reactor.netty.http.client.HttpClient]
 */
@ExperimentalSerializationApi
class MonobankPublicApi internal constructor(
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
        jsonBuilderAction: JsonBuilder.() -> Unit = {},
        rateLimiterConfig: RateLimiterConfig = defaultRateLimiterConfig(),
        reactorNettyConnectionProvider: ConnectionProvider? = null
    ) : this(
        BASE_URL,
        PORT,
        jsonBuilderAction,
        rateLimiterConfig,
        reactorNettyConnectionProvider
    )

    /**
     * Отримати базовий перелік курсів валют monobank.
     * Інформація кешується та оновлюється не частіше 1 разу на 5 хвилин.
     */
    fun getExchangeRates(): Mono<List<MonoCurrencyInfo>> =
        requestExecutor.executeGet(
            buildUri("/bank/currency"),
            json.serializersModule.serializer()
        )
}
