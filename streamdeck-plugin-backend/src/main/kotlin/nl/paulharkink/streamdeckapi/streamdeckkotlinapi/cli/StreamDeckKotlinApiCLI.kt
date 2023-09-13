package nl.paulharkink.streamdeckapi.streamdeckkotlinapi.cli

import nl.paulharkink.streamdeckapi.streamdeckkotlinapi.info
import nl.paulharkink.streamdeckapi.streamdeckkotlinapi.warn
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import picocli.CommandLine

@Profile("cli")
@Component
class StreamDeckKotlinApiCLI(
    private val connectCommand: ConnectCLIOptions
) : CommandLineRunner, ExitCodeGenerator {

    private var exitCode = 0

    override fun getExitCode(): Int {
        return exitCode
    }

    override fun run(vararg args: String) {
        info("You started me with ${args.toList().joinToString(" ")}")
        exitCode = CommandLine(connectCommand).execute(*args)
        warn("Exiting with code $exitCode")
    }

}