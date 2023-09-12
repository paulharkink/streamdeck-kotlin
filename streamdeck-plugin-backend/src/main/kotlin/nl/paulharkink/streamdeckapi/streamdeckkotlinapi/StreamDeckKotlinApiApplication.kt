package nl.paulharkink.streamdeckapi.streamdeckkotlinapi

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import picocli.CommandLine

@SpringBootApplication
class StreamDeckKotlinApiApplication(
    private val connectCommand: ConnectToAPI
) : CommandLineRunner, ExitCodeGenerator {

    private var exitCode = 0

    override fun getExitCode(): Int {
        return exitCode
    }

    override fun run(vararg args: String) {
        info("You started me with ${args.toList().joinToString(" ")}")
        val commandLine = CommandLine(connectCommand)
        val parsed = commandLine.parseArgs(*args)
        info("connectCommand: $connectCommand")

        connectCommand.run()
//        exitCode = commandLine.execute(*args)
        warn("Exiting with code $exitCode")
    }

}

fun main(args: Array<String>) {
    runApplication<StreamDeckKotlinApiApplication>(*args)
}
