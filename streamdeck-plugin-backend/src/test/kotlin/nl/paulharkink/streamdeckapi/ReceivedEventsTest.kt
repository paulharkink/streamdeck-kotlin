package nl.paulharkink.streamdeckapi

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.projectmapk.jackson.module.kogera.KotlinModule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import nl.paulharkink.streamdeckapi.ws.DeviceDidConnect
import nl.paulharkink.streamdeckapi.ws.KeyUp
import nl.paulharkink.streamdeckapi.ws.ReceivedEvent
import nl.paulharkink.streamdeckapi.ws.SystemDidWakeUp


class ReceivedEventsTest : FunSpec() {

    companion object {
        private val objectMapper: ObjectMapper = ObjectMapper()
            .registerModule(KotlinModule.Builder().build())
//            .registerModule(ParameterNamesModule())

        val deviceDidConnectJson = """
            {
              "device" : "10458C7F2BAC0281A90818DBD247F874",
              "deviceInfo" : {
                "name" : "Stream Deck",
                "size" : {
                  "columns" : 5,
                  "rows" : 3
                },
                "type" : 0
              },
              "event" : "deviceDidConnect"
            }
        """.trimIndent()
        val deviceDidConnect = DeviceDidConnect(
            device = DeviceId("10458C7F2BAC0281A90818DBD247F874"),
            deviceInfo = DeviceInfo(
                name = "Stream Deck",
                size = DeviceDimensions(columns = 5, rows = 3),
                type = DeviceType.StreamDeck
            )
        )

        val keyUpJson = """
        {
          "action" : "com.elgato.template.action",
          "context" : "f0b1bd5f1092a3f72909d36576d4a067",
          "device" : "10458C7F2BAC0281A90818DBD247F874",
          "event" : "keyUp",
          "payload" : {
            "coordinates" : {
              "column" : 3,
              "row" : 1
            },
            "isInMultiAction" : false,
            "settings" : {
              "message" : "My message",
              "more" : "",
              "name" : "my name"
            }
          }
        }
        """.trimIndent()

        val keyUp = KeyUp(
            action = Action("com.elgato.template.action"),
            context = Context("f0b1bd5f1092a3f72909d36576d4a067"),
            device = DeviceId("10458C7F2BAC0281A90818DBD247F874"),
            event = "keyUp",
            payload = KeyUp.Payload(
                coordinates = Coordinates(column = 3, row = 1),
                isInMultiAction = false,
                settings = AnyJson(mapOf(
                    "message" to "My message",
                    "more" to "",
                    "name" to "my name"
                ))
            )
        )

        val systemDidWakeUpJson = """
        {
           "event": "systemDidWakeUp"
        }
        """.trimIndent()

        val systemDidWakeUp = SystemDidWakeUp("systemDidWakeUp")

    }

    init {

        test("should parse deviceDidConnect") {
            val res = objectMapper.readValue(deviceDidConnectJson, DeviceDidConnect::class.java)
            res shouldBe deviceDidConnect
        }

        test("should parse KeyUp") {
            val res = objectMapper.readValue(keyUpJson, KeyUp::class.java)
            res shouldBe keyUp
        }

        test("should parse SystemDidWakeUp") {
            val res = objectMapper.readValue(systemDidWakeUpJson, ReceivedEvent::class.java)
            res shouldBe systemDidWakeUp
        }
    }
}