package nl.paulharkink.streamdeckapi.streamdeckkotlinapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import io.github.projectmapk.jackson.module.kogera.KotlinModule
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class InfoTest {
    private val objectMapper: ObjectMapper = ObjectMapper()
        .registerModule(KotlinModule.Builder().build())
        .registerModule(ParameterNamesModule())

    private val application = """
        {
    "font": ".AppleSystemUIFont",
    "language": "en",
    "platform": "mac",
    "platformVersion": "13.5.1",
    "version": "6.3.0.18948"
  }
    """.trimIndent()

    private val colors = """
        {
    "buttonPressedBackgroundColor": "#303030FF",
    "buttonPressedBorderColor": "#646464FF",
    "buttonPressedTextColor": "#969696FF",
    "disabledColor": "#007AFF7F",
    "highlightColor": "#007AFFFF",
    "mouseDownColor": "#2EA8FFFF"
  }
    """.trimIndent()

    private val deviceOne = """
        {
      "id": "10458C7F2BAC0281A90818DBD247F874",
      "name": "Stream Deck",
      "size": {
        "columns": 5,
        "rows": 3
      },
      "type": 0
    }
    """.trimIndent()

    private val plugin = """
        {
    "uuid": "nl.paulharkink.esd.kotlin",
    "version": "1.0.2"
  }
    """.trimIndent()

    private val infoJson = """
        {
  "application": $application,
  "colors": $colors,
  "devicePixelRatio": 1,
  "devices": [
    $deviceOne
  ],
  "plugin": $plugin
}
    """.trimIndent()

    @Test
    fun shouldParsePlugin(){
        objectMapper.readValue(plugin, Plugin::class.java)
    }

    @Test
    fun shouldParseApplication() {
        objectMapper.readValue(application, Application::class.java)
    }

    @Test
    fun shouldParseColor() {
        objectMapper.readValue("\"#303030FF\"", ColorCode::class.java)
    }

    @Test
    fun shouldParseColors(){
        val res = objectMapper.readValue(colors, Colors::class.java)
        assertThat(res, notNullValue())
    }

    @Test
    fun shouldParseInfo() {
        objectMapper.readValue(infoJson, Info::class.java)
    }
}