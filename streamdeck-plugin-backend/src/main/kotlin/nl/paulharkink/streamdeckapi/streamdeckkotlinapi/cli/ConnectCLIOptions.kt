package nl.paulharkink.streamdeckapi.streamdeckkotlinapi.cli

import com.fasterxml.jackson.databind.ObjectMapper
import nl.paulharkink.streamdeckapi.streamdeckkotlinapi.Info
import nl.paulharkink.streamdeckapi.streamdeckkotlinapi.SDConnectionFactory
import nl.paulharkink.streamdeckapi.streamdeckkotlinapi.SDConnectionProperties
import nl.paulharkink.streamdeckapi.streamdeckkotlinapi.info
import org.springframework.stereotype.Component
import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Component
@Command(name = "connect", mixinStandardHelpOptions = true)
class ConnectCLIOptions(
    private val objectMapper: ObjectMapper,
    private val sdConnectionFactory: SDConnectionFactory
) : SDConnectionProperties, Runnable {

    @Option(names = ["-port"], required = true)
    override var port: Int = 0

    @Option(names = ["-pluginUUID"], required = true)
    override var uuid: String = ""

    @Option(names = ["-registerEvent"], required = true)
    override var event: String = ""

    override var info: Info? = null

    @Option(names = ["-info"], required = true)
    fun setInfo(infoJson: String) {
        info("Parsing infoJson: $infoJson")
        info = objectMapper.readValue(infoJson, Info::class.java)
    }


    override fun toString() : String = "ws://localhost:$port uuid=$uuid, event=$event. Info json=${
        if (info == null) "null" else
            objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(info)
    }"

    override fun run() {
        sdConnectionFactory.connectAndWait(this)
    }

}