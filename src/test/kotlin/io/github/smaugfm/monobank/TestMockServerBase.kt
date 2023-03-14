package io.github.smaugfm.monobank

import io.github.smaugfm.monobank.core.RequestExecutor.Companion.X_TOKEN
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.mockserver.configuration.Configuration.configuration
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.NottableString
import org.slf4j.event.Level

@OptIn(ExperimentalSerializationApi::class)
open class TestMockServerBase(private val expectXToken: Boolean = true) {
    protected val apiPersonal = MonobankPersonalApi(TOKEN, BASE_URL, PORT)
    protected val apiPublic = MonobankPublicApi(BASE_URL, PORT)

    @BeforeEach
    fun createClient() {
        mockServer.reset()
        if (expectXToken) {
            mockServer
                .`when`(
                    HttpRequest.request()
                        .withHeader(
                            NottableString.not(X_TOKEN)
                        )
                ).respond(
                    HttpResponse.response()
                        .withStatusCode(401)
                )
            mockServer
                .`when`(
                    HttpRequest.request()
                        .withHeader(
                            NottableString.string(X_TOKEN),
                            NottableString.not(TOKEN)
                        )
                ).respond(
                    HttpResponse.response()
                        .withStatusCode(201)
                )
        }
    }

    companion object {
        const val TOKEN = "validToken"
        const val PORT = 1080

        @JvmStatic
        protected val BASE_URL = "http://127.0.0.1:$PORT"

        @JvmStatic
        protected lateinit var mockServer: ClientAndServer

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            mockServer = ClientAndServer
                .startClientAndServer(
                    configuration()
                        .logLevel(Level.INFO),
                    PORT
                )
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            mockServer.stop()
        }

        fun getResourceAsString(path: String) =
            TestMockServerBase::class.java.classLoader.getResource(path)!!.readText()
    }
}
