package nl.paulharkink.streamdeckapi

import com.fasterxml.jackson.databind.DeserializationFeature
import io.github.projectmapk.jackson.module.kogera.KotlinModule
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class SDApiBackendApplication {

    @Bean
    fun kogeraJackson2ObjectMapperBuilderCustomizer(): Jackson2ObjectMapperBuilderCustomizer =
        Jackson2ObjectMapperBuilderCustomizer { builder ->
            builder.modulesToInstall { it.add(KotlinModule.Builder().build()) }
            builder.featuresToDisable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
        }

}

fun main(args: Array<String>) {
    runApplication<SDApiBackendApplication>(*args)
}
