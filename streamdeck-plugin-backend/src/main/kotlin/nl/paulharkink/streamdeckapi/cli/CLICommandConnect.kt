package nl.paulharkink.streamdeckapi.cli

import com.fasterxml.jackson.databind.ObjectMapper
import nl.paulharkink.streamdeckapi.Info
import nl.paulharkink.streamdeckapi.info
import nl.paulharkink.streamdeckapi.prettyWriteValueAsString
import nl.paulharkink.streamdeckapi.ws.SDConnectionProperties
import nl.paulharkink.streamdeckapi.ws.SDWebSocketConnectionFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Profile("!rest")
@Component
@Command(name = "connect", mixinStandardHelpOptions = true)
class CLICommandConnect(
    private val objectMapper: ObjectMapper,
    private val sdWebSocketConnectionFactory: SDWebSocketConnectionFactory
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


    override fun toString(): String =
        "ws://localhost:$port uuid=$uuid, event=$event. Info json=${
            objectMapper.prettyWriteValueAsString(info)
        }"

    override fun run() {
        sdWebSocketConnectionFactory.connectAndWait(this)
    }

}