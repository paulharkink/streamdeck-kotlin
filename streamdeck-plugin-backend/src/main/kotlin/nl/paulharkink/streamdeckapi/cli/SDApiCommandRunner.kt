package nl.paulharkink.streamdeckapi.cli

import nl.paulharkink.streamdeckapi.info
import nl.paulharkink.streamdeckapi.warn
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import picocli.CommandLine

@Profile("!rest")
@Component
class SDApiCommandRunner(
    private val connectCommand: CLICommandConnect
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