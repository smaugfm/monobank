package io.github.smaugfm.monobank

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.smaugfm.monobank.model.MonoCurrencyInfo
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.jupiter.api.Test
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.mockserver.model.MediaType
import java.util.Currency

@ExperimentalSerializationApi
class MonobankPublicApiTest : TestMockServerBase(false) {

    @Test
    fun getExchangeRatesTest() {
        mockServer.`when`(
            request("/bank/currency")
                .withMethod("GET")
        ).respond(
            response()
                .withStatusCode(200)
                .withContentType(MediaType.APPLICATION_JSON_UTF_8)
                .withBody(getResourceAsString("getExchangeRates.json"))
        )

        assertThat(apiPublic.getExchangeRates().block())
            .isEqualTo(
                listOf(
                    MonoCurrencyInfo(
                        Currency.getInstance("USD"),
                        Currency.getInstance("UAH"),
                        Instant.fromEpochSeconds(1552392228),
                        27.0,
                        27.2,
                        27.1
                    )
                )
            )
    }
}
