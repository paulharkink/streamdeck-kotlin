package nl.paulharkink.streamdeckapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import io.github.projectmapk.jackson.module.kogera.KotlinModule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import nl.paulharkink.streamdeckapi.rest.RestSDConnectionProperties


class RestSDConnectionPropertiesTest : FunSpec() {

    companion object {
        private val objectMapper: ObjectMapper = ObjectMapper()
            .registerModule(KotlinModule.Builder().build())
            .registerModule(ParameterNamesModule())

        val sdConnectionPropertiesJson = """
        {
          "port": "28196",
          "pluginUUID": "74E3F202E0461D1B4704C95BEA2A9ECD",
          "registerEvent": "registerPlugin",
          "info": ${InfoTest.infoJson}
        }
        """.trimIndent()

        val sdConnectionProperties = RestSDConnectionProperties(
            port = 28196,
            uuid = "74E3F202E0461D1B4704C95BEA2A9ECD",
            event = "registerPlugin",
            info = InfoTest.info
        )

    }

    init {
        test("should parse SdConnectionProperties") {
            val res = objectMapper.readValue(sdConnectionPropertiesJson, RestSDConnectionProperties::class.java)
            res shouldBe sdConnectionProperties
        }

    }
}