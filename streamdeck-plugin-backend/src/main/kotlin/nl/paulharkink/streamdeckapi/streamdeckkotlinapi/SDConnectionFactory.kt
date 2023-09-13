package nl.paulharkink.streamdeckapi.streamdeckkotlinapi

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.socket.*
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import java.net.URI
import java.util.concurrent.CountDownLatch

@Service
class SDConnectionFactory(
    private val objectMapper: ObjectMapper
) {

    fun connect(connectionProperties: SDConnectionProperties): Connection {
        val connection = Connection(objectMapper, connectionProperties)
        connection.waitToBeConnected()
        connection.sendMessage(
            RegisterEvent(
                connectionProperties.event,
                connectionProperties.uuid
            )
        )
        return connection;
    }

    fun connectAndWait(connectionProperties: SDConnectionProperties) {
        info("Connecting")
        val connection = connect(connectionProperties)
        connection.waitToBeDisconnected()
        warn("Disconnected")
    }
}

class Connection(
    private val objectMapper: ObjectMapper,
    connectionProperties: SDConnectionProperties
) {

    private val wsHandler: WSHandler
    private var ses: WebSocketSession

    init {
        wsHandler = WSHandler()
        ses = StandardWebSocketClient()
            .execute(wsHandler, null, URI.create("ws://localhost:${connectionProperties.port}"))
            .get()
    }

    fun <T : Any> sendMessage(msg: T) {
        ses.sendMessage(
            TextMessage(
                objectMapper.writeValueAsString(msg)
            )
        )
    }

    fun waitToBeConnected() = wsHandler.waitToBeConnected()
    fun waitToBeDisconnected() = wsHandler.waitToBeDisconnected()

    // Hide the WebSocketHandler implementation from Connection
    private inner class WSHandler : WebSocketHandler {

        val answered = CountDownLatch(1)
        val hungUp = CountDownLatch(1)

        fun waitToBeConnected() = answered.await()
        fun waitToBeDisconnected() = hungUp.await()

        override fun afterConnectionEstablished(session: WebSocketSession) {
            info("afterConnectionEstablished(session = $session)")
            answered.countDown()
        }

        override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
            val payloadString = (message as? TextMessage)?.payload?.let {
                objectMapper.prettyPrintJson(
                    it
                )
            } ?: message
            info(
                "handleMessage(session = $session, message= $payloadString )"
            )
        }

        override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
            info("handleTransportError(session = $session, exception= $exception)")
        }

        override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
            info("afterConnectionClosed(session = $session, closeStatus= $closeStatus)")
            hungUp.countDown()
        }

        override fun supportsPartialMessages(): Boolean {
            info("supportsPartialMessages()")
            return false
        }
    }
}

fun ObjectMapper.prettyPrintJson(json: String): String {
    return try {
        writerWithDefaultPrettyPrinter()
            .writeValueAsString(
                readTree(json)
            )
    } catch (e: Exception) {
        warn("Invalid JSON: $json", e)
        json
    }
}

interface SDConnectionProperties {
    var port: Int

    var uuid: String

    var event: String

    var info: Info?
}
