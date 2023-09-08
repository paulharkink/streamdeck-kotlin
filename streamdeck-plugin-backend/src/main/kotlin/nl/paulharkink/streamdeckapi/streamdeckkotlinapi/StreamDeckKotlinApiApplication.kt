package nl.paulharkink.streamdeckapi.streamdeckkotlinapi

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.net.URI
import java.util.concurrent.CompletableFuture

@SpringBootApplication
class StreamDeckKotlinApiApplication(
    private val connectCommand: ConnectToAPI,
    private val objectMapper: ObjectMapper
) : CommandLineRunner, ExitCodeGenerator {

    private var exitCode = 0

    override fun getExitCode(): Int {
        return exitCode
    }

    @Component
    @Command(name = "connect", mixinStandardHelpOptions = true)
    class ConnectToAPI(private val objectMapper: ObjectMapper) : Runnable {
        @Option(names = ["-port"], required = true)
        private var port: Int = 0

        @Option(names = ["-pluginUUID"], required = true)
        private var uuid: String = ""

        @Option(names = ["-registerEvent"], required = true)
        private var event: String = ""

        var info: Info? = null

        @Option(names = ["-info"], required = true)
        fun setInfo(infoJson: String) {
            info = objectMapper.readValue(infoJson, Info::class.java)
        }

        override fun run() {
            println(
                "Connecting to $port using $uuid, with event $event. Info json: ${
                    if (info == null) "null" else
                        objectMapper
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(info)
                }"
            )
            val wsClient = StandardWebSocketClient()
            val res: CompletableFuture<WebSocketSession> = wsClient.execute(object : WebSocketHandler {
                override fun afterConnectionEstablished(session: WebSocketSession) {
                    println("afterConnectionEstablished(session = $session)")
                }

                override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
                    println("handleMessage(session = $session, message= $message)")
                }

                override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
                    println("handleTransportError(session = $session, exception= $exception)")
                }

                override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
                    println("afterConnectionClosed(session = $session, closeStatus= $closeStatus)")
                }

                override fun supportsPartialMessages(): Boolean {
                    println("supportsPartialMessages()")
                    return false
                }
            }, null, URI.create("ws://localhost:$port"))

            val ses: WebSocketSession = res.get()
//            ses.sendMessage(WebSocketMessage())
            println("${res.get()}")

        }


    }

    override fun run(vararg args: String) {
        println("You started me with ${args.toList()}")

        exitCode = CommandLine(connectCommand).execute(*args)
    }

}

data class Info(
    val application: Application,
    val plugin: Plugin,
    val devicePixelRatio: Int
)

enum class Platform {
    win, mac
}

data class Application(
    val font: String,
    val language: String,
    val platform: Platform,
    val platformVersion: String,
    val version: String,
    val colors: Colors,
    val devices: List<Device>
)

data class Plugin(val uuid: String, val version: String)

data class Colors(
    val buttonPressedBackgroundColor: ColorCode,
    val buttonPressedBorderColor: ColorCode,
    val buttonPressedTextColor: ColorCode,
    val disabledColor: ColorCode,
    val highlightColor: ColorCode,
    val mouseDownColor: ColorCode
)

data class Device(
    val id: String,
    val name: String,
    val size: DeviceDimensions,
    val type: Int
)

data class DeviceDimensions(val columns: Int, val rows: Int)

@JvmInline
value class ColorCode(private val code: String) {
    init {
        require("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$".toRegex().matches(code)) { "Invalid color code" }
    }
}


fun main(args: Array<String>) {
    runApplication<StreamDeckKotlinApiApplication>(*args)
}
