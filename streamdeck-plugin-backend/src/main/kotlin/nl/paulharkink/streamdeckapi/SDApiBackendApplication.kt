package nl.paulharkink.streamdeckapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SDApiBackendApplication

fun main(args: Array<String>) {
    runApplication<SDApiBackendApplication>(*args)
}
