package nl.paulharkink.streamdeckapi.streamdeckkotlinapi

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
//    private val factory: CommandLine.IFactory,
    private val connectCommand: ConnectToAPI
) : CommandLineRunner, ExitCodeGenerator {

    private var exitCode = 0

    override fun getExitCode(): Int {
        return exitCode
    }

    @Component
    @Command(name = "connect", mixinStandardHelpOptions = true)
    class ConnectToAPI : Runnable {
        @Option(names = ["-port"], required = true)
        private var port: Int = 0

        @Option(names = ["-pluginUUID"], required = true)
        private var uuid: String = ""

        @Option(names = ["-registerEvent"], required = true)
        private var event: String = ""

        @Option(names = ["-info"], required = true)
        private var info: String = ""

        override fun run() {
            println("Connecting to $port using $uuid, with event $event. Info json: $info")
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

            val ses : WebSocketSession = res.get()
//            ses.sendMessage(WebSocketMessage())
            println("${res.get()}")

        }


    }

    override fun run(vararg args: String) {
        println("You started me with ${args.toList()}")

        exitCode = CommandLine(connectCommand).execute(*args)
//        exitCode = CommandLine(connectCommand, factory).execute(*args)
    }

}


fun main(args: Array<String>) {
    runApplication<StreamDeckKotlinApiApplication>(*args)
}
