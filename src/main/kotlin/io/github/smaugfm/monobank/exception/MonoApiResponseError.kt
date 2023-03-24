package io.github.smaugfm.monobank.exception

import reactor.netty.http.client.HttpClientResponse

class MonoApiResponseError(
    response: HttpClientResponse,
    body: ByteArray
) : Error("${response.status()}\n${body.decodeToString()}")
