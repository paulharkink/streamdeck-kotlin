package nl.paulharkink.streamdeckapi.ws

import com.fasterxml.jackson.databind.ObjectMapper
import nl.paulharkink.streamdeckapi.Info
import nl.paulharkink.streamdeckapi.info
import nl.paulharkink.streamdeckapi.prettify
import nl.paulharkink.streamdeckapi.warn
import org.springframework.stereotype.Service
import org.springframework.web.socket.*
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import java.net.URI
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

@Service
class SDWebSocketConnectionFactory(
    private val objectMapper: ObjectMapper
) {

    fun connect(connectionProperties: SDConnectionProperties): SDWebSocketConnection =
        SDWebSocketConnection.connect(objectMapper, connectionProperties)

    fun connectAndWait(connectionProperties: SDConnectionProperties) {
        val connection = connect(connectionProperties)
        connection.waitToBeDisconnected()
        warn("Disconnected")
    }
}

class SDWebSocketConnection private constructor(
    private val objectMapper: ObjectMapper,
    private val lowLevelWebSocketHandler: LowLevelWebSocketHandler,
    private val ses: WebSocketSession
) {

    companion object {
        fun connect(
            objectMapper: ObjectMapper,
            connectionProperties: SDConnectionProperties
        ): SDWebSocketConnection {
            val lowLevelWebSocketHandler = LowLevelWebSocketHandler(objectMapper)
            val ses: WebSocketSession = establishConnection(lowLevelWebSocketHandler, connectionProperties)

            val connection = SDWebSocketConnection(objectMapper, lowLevelWebSocketHandler, ses)
            connection.sendMessage(
                RegisterEvent(
                    connectionProperties.event,
                    connectionProperties.uuid
                )
            )
            return connection
        }

        private fun establishConnection(
            lowLevelWebSocketHandler: LowLevelWebSocketHandler,
            connectionProperties: SDConnectionProperties
        ): WebSocketSession = StandardWebSocketClient()
            .execute(lowLevelWebSocketHandler, null, URI.create("ws://localhost:${connectionProperties.port}"))
            .get()
    }

    init {
        lowLevelWebSocketHandler.connection = this
    }

    fun <T : Any> sendMessage(msg: T) {
        val msgJson = objectMapper.writeValueAsString(msg)
        info("Sending $msg")
        ses.sendMessage(TextMessage(msgJson))
    }

    fun close() = ses.close(CloseStatus.NORMAL)

    fun waitToBeDisconnected(duration: Duration = Duration.INFINITE) = lowLevelWebSocketHandler.waitToBeDisconnected(duration)
}

// Hide the WebSocketHandler implementation from Connection
private class LowLevelWebSocketHandler(private val objectMapper: ObjectMapper) : WebSocketHandler {

    lateinit var connection: SDWebSocketConnection

    val hungUp = CountDownLatch(1)

    fun waitToBeDisconnected(duration: Duration = Duration.INFINITE) =
        if (duration.isFinite())
            hungUp.await(duration.inWholeMilliseconds, TimeUnit.MILLISECONDS)
        else
            hungUp.await()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        info("afterConnectionEstablished(session = $session)")
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        (message as? TextMessage)?.let { textMessage:TextMessage ->
            try {
                val receivedEvent = objectMapper.readValue(textMessage.payload, ReceivedEvent::class.java)
                info(
                    "handleMessage(session = $session, message= $receivedEvent )"
                )
            } catch (e : Exception) {
                warn("Could not parse as ReceivedMessage: ${objectMapper.prettify(textMessage.payload)}", e)
            }

        } ?: run {
            warn("Received message that was not a TextMessage: $message")
        }
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

interface SDConnectionProperties {
    val port: Int

    val uuid: String

    val event: String

    val info: Info
}
