package nl.paulharkink.streamdeckapi.cli

import com.fasterxml.jackson.databind.ObjectMapper
import nl.paulharkink.streamdeckapi.info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Profile("!rest")
@Configuration
class CliSpecificConfig {

    @Bean
    fun restTemplate(objectMapper: ObjectMapper): RestTemplate {
        val restTemplate = RestTemplate()
        restTemplate.messageConverters.find { converter -> converter is MappingJackson2HttpMessageConverter }
            ?.let { converter ->
                info("Overwriting objectMapper of")
                (converter as MappingJackson2HttpMessageConverter).objectMapper = objectMapper }
        return restTemplate
    }
}