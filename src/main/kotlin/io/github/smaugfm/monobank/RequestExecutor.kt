package io.github.smaugfm.monobank

import io.github.smaugfm.monobank.exception.MonoApiResponseError
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.handler.codec.http.HttpMethod
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import reactor.core.publisher.Mono
import reactor.netty.ByteBufMono
import reactor.netty.http.client.HttpClient
import reactor.netty.http.client.HttpClientResponse
import java.io.ByteArrayOutputStream
import java.net.URI

@ExperimentalSerializationApi
internal class RequestExecutor(
    private val port: Int,
    private val json: Json,
    private val httpClient: HttpClient
) {

    fun <TResponse : Any> executeGet(
        uri: URI,
        responseSerializer: KSerializer<TResponse>? = null,
        token: String? = null,
    ): Mono<TResponse> =
        httpClient
            .port(port)
            .headers { if (token != null) it.add(X_TOKEN, token) }
            .request(HttpMethod.GET)
            .uri(uri)
            .responseSingle { resp, byteBufMono ->
                processResponse(resp, byteBufMono, responseSerializer)
            }

    fun <TBody, TResponse : Any> executePost(
        uri: URI,
        body: TBody,
        bodySerializer: KSerializer<TBody>,
        responseSerializer: KSerializer<TResponse>? = null,
        token: String? = null,
    ): Mono<TResponse> =
        httpClient
            .port(port)
            .headers { it.add(X_TOKEN, token) }
            .request(HttpMethod.POST)
            .uri(uri)
            .send(Mono.just(serializeRequestBody(bodySerializer, body)))
            .responseSingle { resp, byteBufMono ->
                processResponse(resp, byteBufMono, responseSerializer)
            }

    private fun <TResponse : Any> processResponse(
        resp: HttpClientResponse,
        byteBufMono: ByteBufMono,
        responseSerializer: KSerializer<TResponse>?
    ): Mono<TResponse> =
        if (isOk(resp)) {
            getBody(responseSerializer, byteBufMono)
        } else {
            byteBufMono.asByteArray()
                .flatMap<TResponse> {
                    Mono.error(MonoApiResponseError(resp, it))
                }.switchIfEmpty(
                    Mono.error(MonoApiResponseError(resp, byteArrayOf()))
                )
        }

    private fun <TResponse : Any> getBody(
        responseSerializer: KSerializer<TResponse>?,
        byteBufMono: ByteBufMono
    ): Mono<TResponse> = if (responseSerializer != null) byteBufMono
        .asString()
        .map { body: String ->
            json.decodeFromString(responseSerializer, body)
        }
    else Mono.empty()

    private fun <TResponse : Any> deserializeBody(
        responseSerializer: KSerializer<TResponse>?,
        byteBufMono: ByteBufMono
    ): Mono<TResponse> {
        return if (responseSerializer != null) byteBufMono
            .asString()
            .map { body: String ->
                json.decodeFromString(responseSerializer, body)
            }
        else Mono.empty()
    }

    private fun <T> serializeRequestBody(serializer: KSerializer<T>, body: T): ByteBuf {
        val os = ByteArrayOutputStream()
        json.encodeToStream(serializer, body, os)
        val byteArray = os.toByteArray()

        return Unpooled.wrappedBuffer(byteArray)
    }

    private fun isOk(response: HttpClientResponse) =
        response.status().codeAsText().startsWith("2")

    companion object {
        const val X_TOKEN = "X-Token"
    }
}
