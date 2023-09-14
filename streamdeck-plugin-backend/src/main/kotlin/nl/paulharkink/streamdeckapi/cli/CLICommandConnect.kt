package nl.paulharkink.streamdeckapi.cli

import com.fasterxml.jackson.databind.ObjectMapper
import nl.paulharkink.streamdeckapi.Info
import nl.paulharkink.streamdeckapi.info
import nl.paulharkink.streamdeckapi.prettyWriteValueAsString
import nl.paulharkink.streamdeckapi.rest.RestSDConnectionProperties
import nl.paulharkink.streamdeckapi.ws.SDConnectionProperties
import nl.paulharkink.streamdeckapi.ws.SDWebSocketConnectionFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.net.URI

@Profile("!rest")
@Component
@Command(name = "connect", mixinStandardHelpOptions = true)
class CLICommandConnect(
    private val objectMapper: ObjectMapper,
    private val sdWebSocketConnectionFactory: SDWebSocketConnectionFactory,
    private val restTemplate: RestTemplate
) : SDConnectionProperties, Runnable {

    @Option(names = ["-port"], required = true)
    override var port: Int = 0

    @Option(names = ["-pluginUUID"], required = true)
    override var uuid: String = ""

    @Option(names = ["-registerEvent"], required = true)
    override var event: String = ""

    override lateinit var info: Info

    @Option(names = ["-info"], required = true)
    fun setInfo(infoJson: String) {
        info("Parsing infoJson: $infoJson")
        info = objectMapper.readValue(infoJson, Info::class.java)
    }

    @Option(names = ["-backend"])
    var backend: URI? = null


    override fun toString(): String =
        "ws://localhost:$port uuid=$uuid, event=$event. Info json=${
            objectMapper.prettyWriteValueAsString(info)
        }"

    override fun run() {
        backend?.let { connectToRunningBackend(it) }
            ?: run { startBackend() }
    }

    private fun startBackend() {
        info("Starting up a backend: $this")
        sdWebSocketConnectionFactory.connectAndWait(this)
        info("Connection shut down")
    }

    private fun connectToRunningBackend(uri: URI) {
        val connectionProperties = RestSDConnectionProperties(
            port = port,
            uuid = uuid,
            event = event,
            info = info
        )
        info(
            "Trying to POST to http://localhost:8080/connect ${
                objectMapper.prettyWriteValueAsString(
                    connectionProperties
                )
            }"
        )
        val res = restTemplate.postForObject(uri, connectionProperties, String::class.java)
        info("Done. Result was \"$res\"")
    }

}