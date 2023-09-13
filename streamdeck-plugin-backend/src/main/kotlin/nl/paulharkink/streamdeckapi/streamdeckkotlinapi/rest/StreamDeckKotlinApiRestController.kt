package nl.paulharkink.streamdeckapi.streamdeckkotlinapi.rest

import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@Profile("rest")
@RestController
class StreamDeckKotlinApiRestController {

    @PostMapping("/connect")
    fun connect() {

    }

}