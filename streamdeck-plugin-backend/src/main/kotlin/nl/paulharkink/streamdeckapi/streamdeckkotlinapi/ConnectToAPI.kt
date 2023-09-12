package nl.paulharkink.streamdeckapi.streamdeckkotlinapi

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.net.URI
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CountDownLatch

@Component
@Command(name = "connect", mixinStandardHelpOptions = true)
class ConnectToAPI(private val objectMapper: ObjectMapper) : Runnable, WebSocketHandler {

    private val latch = CountDownLatch(1)

    @Option(names = ["-port"], required = true)
    private var port: Int = 0

    @Option(names = ["-pluginUUID"], required = true)
    private var uuid: String = ""

    @Option(names = ["-registerEvent"], required = true)
    private var event: String = ""

    var info: Info? = null

    @Option(names = ["-info"], required = true)
    fun setInfo(infoJson: String) {
        info("Parsing infoJson: $infoJson")
        info = objectMapper.readValue(infoJson, Info::class.java)
    }

    override fun run() {
        info("Connecting")
        val wsClient = StandardWebSocketClient()
        val res: CompletableFuture<WebSocketSession> = wsClient.execute(this, null, URI.create("ws://localhost:$port"))

        val ses: WebSocketSession = res.get()
        ses.sendMessage(TextMessage(objectMapper.writeValueAsString(RegisterEvent(this.event, this.uuid))))

        latch.await()
        warn("Disconnected")
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        info("afterConnectionEstablished(session = $session)")
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        info("handleMessage(session = $session, message= $message)")
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        info("handleTransportError(session = $session, exception= $exception)")
    }

    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        info("afterConnectionClosed(session = $session, closeStatus= $closeStatus)")
        latch.countDown()
    }

    override fun supportsPartialMessages(): Boolean {
        info("supportsPartialMessages()")
        return false
    }

    override fun toString() : String = "ws://localhost:$port uuid=$uuid, event=$event. Info json=${
        if (info == null) "null" else
            objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(info)
    }"

}