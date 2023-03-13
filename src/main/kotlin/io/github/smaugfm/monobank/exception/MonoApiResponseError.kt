package io.github.smaugfm.monobank.exception

import reactor.netty.http.client.HttpClientResponse

class MonoApiResponseError(
    val response: HttpClientResponse,
    val body: ByteArray
) : Error() {
    override fun toString(): String {
        return "${response.status()}\n${body.decodeToString()}"
    }
}
