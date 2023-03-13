package io.github.smaugfm.monobank

import io.github.smaugfm.monobank.model.MonoCurrencyInfo
import io.github.smaugfm.monobank.model.MonoStatementItem
import io.github.smaugfm.monobank.model.MonoUserInfo
import io.github.smaugfm.monobank.model.MonoWebhookRequest
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.serializer
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.net.URI

@OptIn(ExperimentalSerializationApi::class)
open class Monobank(
    private val token: String,
    private val baseUrl: String = "https://api.monobank.ua",
    port: Int,
    jsonBuilderAction: JsonBuilder.() -> Unit = {},
    reactorNettyConnectionProvider: ConnectionProvider? = null
) {
    private val json = Json(builderAction = jsonBuilderAction)

    private val requestExecutor = buildRequestExecutor(reactorNettyConnectionProvider, port)

    fun getClientStatements(
        account: String,
        from: Instant,
        to: Instant? = null
    ): Mono<List<MonoStatementItem>> {
        var path = "/personal/statement/$account/${from.epochSeconds}"
        if (to != null)
            path += "${to.epochSeconds}"
        return requestExecutor.executeGet(
            buildUri(path),
            json.serializersModule.serializer(),
            token
        )
    }

    fun setClientWebhook(url: String): Mono<Void> =
        requestExecutor.executePost(
            buildUri("/personal/webhook"),
            MonoWebhookRequest(url),
            json.serializersModule.serializer(),
            null,
            token
        )

    fun getExchangeRates(): Mono<List<MonoCurrencyInfo>> =
        requestExecutor.executeGet(
            buildUri("/bank/currency"),
            json.serializersModule.serializer()
        )

    fun getClientInformation(): Mono<MonoUserInfo> =
        requestExecutor.executeGet(
            buildUri("/personal/client-info"),
            json.serializersModule.serializer(),
            token
        )

    private fun buildUri(pathSegments: String, query: String? = null): URI {
        var url = "$baseUrl$pathSegments"
        if (query != null)
            url += "?$query"
        return URI(url)
    }

    private fun buildRequestExecutor(
        reactorNettyConnectionProvider: ConnectionProvider?,
        port: Int
    ) = RequestExecutor(
        port,
        json,
        if (reactorNettyConnectionProvider != null) {
            HttpClient.create(reactorNettyConnectionProvider).baseUrl(baseUrl)
        } else {
            HttpClient.create().baseUrl(baseUrl)
        }
    )

}
