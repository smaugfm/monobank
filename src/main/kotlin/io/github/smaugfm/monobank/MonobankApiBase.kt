package io.github.smaugfm.monobank

import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.ratelimiter.RateLimiterConfig
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator
import io.github.smaugfm.monobank.core.RequestExecutor
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.net.URI
import java.time.Duration
import kotlin.reflect.KCallable

@ExperimentalSerializationApi
sealed class MonobankApiBase(
    private val baseUrl: String = BASE_URL,
    port: Int,
    jsonBuilderAction: JsonBuilder.() -> Unit,
    private val rateLimiterConfig: RateLimiterConfig,
    reactorNettyConnectionProvider: ConnectionProvider?
) {
    protected val json = Json(builderAction = jsonBuilderAction)

    protected val requestExecutor = RequestExecutor(
        port,
        json,
        if (reactorNettyConnectionProvider != null) {
            HttpClient.create(reactorNettyConnectionProvider).baseUrl(baseUrl)
        } else {
            HttpClient.create().baseUrl(baseUrl)
        }
    )

    private lateinit var rateLimiters: Map<String, RateLimiter>

    protected fun buildUri(pathSegments: String, query: String? = null): URI {
        var url = "$baseUrl$pathSegments"
        if (query != null) {
            url += "?$query"
        }
        return URI(url)
    }

    protected fun initRateLimiters(vararg functions: KCallable<Any>) {
        rateLimiters = functions.associate { it.name to RateLimiter.of(it.name, rateLimiterConfig) }
    }

    protected fun <T> Mono<T>.withRateLimiter(callable: KCallable<Any>): Mono<T> =
        this.transformDeferred(RateLimiterOperator.of(rateLimiters[callable.name]!!))

    companion object {
        const val BASE_URL = "https://api.monobank.ua"
        const val PORT = 443

        @JvmStatic
        protected fun defaultRateLimiterConfig(
            secondsPeriod: Long = 65
        ): RateLimiterConfig =
            RateLimiterConfig.custom()
                .limitForPeriod(1)
                .timeoutDuration(Duration.ofSeconds(secondsPeriod))
                .limitRefreshPeriod(Duration.ofSeconds(secondsPeriod))
                .build()
    }
}
