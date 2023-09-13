package nl.paulharkink.streamdeckapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import io.github.projectmapk.jackson.module.kogera.KotlinModule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe


class InfoTest : FunSpec() {

    companion object {
        val objectMapper: ObjectMapper = ObjectMapper()
            .registerModule(KotlinModule.Builder().build())
            .registerModule(ParameterNamesModule())

        val applicationJson = """
        {
            "font": ".AppleSystemUIFont",
            "language": "en",
            "platform": "mac",
            "platformVersion": "13.5.1",
            "version": "6.3.0.18948"
        }
        """.trimIndent()
        val application = Application(
            font = ".AppleSystemUIFont",
            language = "en",
            platform = Platform.MAC,
            platformVersion = "13.5.1",
            version = "6.3.0.18948"
        )

        val colorsJson = """
        {
            "buttonPressedBackgroundColor": "#303030FF",
            "buttonPressedBorderColor": "#646464FF",
            "buttonPressedTextColor": "#969696FF",
            "disabledColor": "#007AFF7F",
            "highlightColor": "#007AFFFF",
            "mouseDownColor": "#2EA8FFFF"
        }
        """.trimIndent()

        val colors = Colors(
            buttonPressedBackgroundColor = ColorCode("#303030FF"),
            buttonPressedBorderColor = ColorCode("#646464FF"),
            buttonPressedTextColor = ColorCode("#969696FF"),
            disabledColor = ColorCode("#007AFF7F"),
            highlightColor = ColorCode("#007AFFFF"),
            mouseDownColor = ColorCode("#2EA8FFFF")
        )

        val deviceOneJson = """
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

        val deviceOne = Device(
            id = "10458C7F2BAC0281A90818DBD247F874",
            name = "Stream Deck",
            size = DeviceDimensions(5, 3),
            type = 0
        )

        val pluginJson = """
        {
            "uuid": "nl.paulharkink.esd.kotlin",
            "version": "1.0.2"
          }
        """.trimIndent()

        val plugin = Plugin(
            uuid = "nl.paulharkink.esd.kotlin",
            version = "1.0.2"
        )

        val infoJson = """
        {
          "application": $applicationJson,
          "colors": $colorsJson,
          "devicePixelRatio": 1,
          "devices": [
            $deviceOneJson
          ],
          "plugin": $pluginJson
        }
        """.trimIndent()

        val info = Info(
            application = application,
            colors = colors,
            devicePixelRatio = 1,
            devices = listOf(deviceOne),
            plugin = plugin
        )
    }

    init {
        test("should parse Plugin") {
            val res = objectMapper.readValue(pluginJson, Plugin::class.java)
            res shouldBe plugin
        }

        test("should parse Application") {
            val res = objectMapper.readValue(applicationJson, Application::class.java)
            res shouldBe application
        }

        test("should parse Color") {
            val res = objectMapper.readValue("\"#303030FF\"", ColorCode::class.java)
            res shouldBe ColorCode("#303030FF")
        }

        test("should parse Device") {
            val res = objectMapper.readValue(deviceOneJson, Device::class.java)
            res shouldBe deviceOne
        }

        test("should parse Colors") {
            val res = objectMapper.readValue(colorsJson, Colors::class.java)
            res shouldBe colors
        }

        test("should parse Info") {
            val res = objectMapper.readValue(infoJson, Info::class.java)
            res shouldBe info
        }

        test("should parse Single Line Json") {
            val res = objectMapper.readValue(
                """
            {"application":{"font":".AppleSystemUIFont","language":"en","platform":"mac","platformVersion":"13.5.1","version":"6.3.0.18948"},"colors":{"buttonPressedBackgroundColor":"#303030FF","buttonPressedBorderColor":"#646464FF","buttonPressedTextColor":"#969696FF","disabledColor":"#007AFF7F","highlightColor":"#007AFFFF","mouseDownColor":"#2EA8FFFF"},"devicePixelRatio":1,"devices":[{"id":"10458C7F2BAC0281A90818DBD247F874","name":"Stream Deck","size":{"columns":5,"rows":3},"type":0}],"plugin":{"uuid":"nl.paulharkink.esd.kotlin","version":"1.0.2"}}
        """.trimIndent(), Info::class.java
            )
            res shouldBe info
        }

    }
}