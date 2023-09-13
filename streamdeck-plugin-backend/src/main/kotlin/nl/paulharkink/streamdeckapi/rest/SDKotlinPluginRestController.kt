package nl.paulharkink.streamdeckapi.rest

import com.fasterxml.jackson.annotation.JsonProperty
import nl.paulharkink.streamdeckapi.Info
import nl.paulharkink.streamdeckapi.info
import nl.paulharkink.streamdeckapi.ws.SDConnectionProperties
import nl.paulharkink.streamdeckapi.ws.SDWebSocketConnection
import nl.paulharkink.streamdeckapi.ws.SDWebSocketConnectionFactory
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Profile("rest")
@RestController
class SDKotlinPluginRestController(private val sdWebSocketConnectionFactory: SDWebSocketConnectionFactory) {

    var connection: SDWebSocketConnection? = null

    init {
        info("Launching the REST controller")
    }

    @PostMapping("/connect")
    fun connect(@RequestBody connectionProperties: RestSDConnectionProperties) {
        connection?.let {
            info("Disconnecting existing connection before attempting to connect again")
            it.close()
            it.waitToBeDisconnected((5).toDuration(DurationUnit.SECONDS))
        }
        connection = sdWebSocketConnectionFactory.connect(connectionProperties)
    }

}

data class RestSDConnectionProperties(
    override var port: Int,
    @JsonProperty("pluginUUID") override var uuid: String,
    @JsonProperty("registerEvent") override var event: String,
    override var info: Info
) : SDConnectionProperties