package io.github.smaugfm.monobank

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.smaugfm.monobank.model.MonoAccount
import io.github.smaugfm.monobank.model.MonoCashbackType
import io.github.smaugfm.monobank.model.MonoClientInformation
import io.github.smaugfm.monobank.model.MonoJar
import io.github.smaugfm.monobank.model.MonoStatementItem
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.jupiter.api.Test
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.mockserver.model.JsonBody.json
import org.mockserver.model.MediaType
import reactor.test.StepVerifier
import java.util.Currency

@ExperimentalSerializationApi
class MonobankPersonalApiTest : TestMockServerBase(true) {

    @Test
    fun getClientInformationTest() {
        mockServer.`when`(
            request("/personal/client-info")
                .withMethod("GET")
        ).respond(
            response()
                .withStatusCode(200)
                .withContentType(MediaType.APPLICATION_JSON_UTF_8)
                .withBody(getResourceAsString("getClientInfo.json"))
        )

        assertThat(apiPersonal.getClientInformation().block())
            .isEqualTo(
                MonoClientInformation(
                    "3MSaMMtczs",
                    "Мазепа Іван",
                    "https://example.com/some_random_data_for_security",
                    listOf(
                        MonoAccount(
                            "kKGVoZuHWzqVoZuH",
                            "uHWzqVoZuH",
                            10000000,
                            10000000,
                            "black",
                            Currency.getInstance("UAH"),
                            MonoCashbackType.UAH,
                            "UA733220010000026201234567890",
                            listOf(
                                "537541******1234"
                            )
                        )
                    ),
                    "psfj",
                    listOf(
                        MonoJar(
                            "kKGVoZuHWzqVoZuH",
                            "uHWzqVoZuH",
                            "На тепловізор",
                            "На тепловізор",
                            Currency.getInstance("UAH"),
                            1000000,
                            10000000
                        )
                    )
                )
            )
    }

    @Test
    fun setClientWebhookTest() {
        val url = "https://example.com/some_random_data_for_security"
        mockServer.`when`(
            request("/personal/webhook")
                .withBody(
                    json(
                        """
                  {
                      "webHookUrl": "$url"
                  }
                        """.trimIndent()
                    )
                )
                .withMethod("POST")
        ).respond(
            response()
                .withStatusCode(200)
        )

        StepVerifier.create(apiPersonal.setClientWebhook(url))
            .verifyComplete()
    }

    @Test
    fun getClientStatementsTest() {
        val account = "test"
        val from = Clock.System.now()
        val to = Clock.System.now()
        mockServer.`when`(
            request("/personal/statement/$account/${from.epochSeconds}/${to.epochSeconds}")
                .withMethod("GET")
        ).respond(
            response()
                .withStatusCode(200)
                .withContentType(MediaType.APPLICATION_JSON_UTF_8)
                .withBody(getResourceAsString("getClientStatements.json"))
        )

        assertThat(apiPersonal.getClientStatements(account, from, to).block())
            .isEqualTo(
                listOf(
                    MonoStatementItem(
                        "ZuHWzqkKGVo=",
                        Instant.fromEpochSeconds(1554466347),
                        "Покупка щастя",
                        7997,
                        7997,
                        false,
                        -95000,
                        -95000,
                        Currency.getInstance("UAH"),
                        0,
                        19000,
                        10050000,
                        "За каву",
                        "XXXX-XXXX-XXXX-XXXX",
                        "2103.в.27",
                        "3096889974",
                        "UA898999980000355639201001404",
                        "ТОВАРИСТВО З ОБМЕЖЕНОЮ ВІДПОВІДАЛЬНІСТЮ «ВОРОНА»"
                    )
                )
            )
    }
}
