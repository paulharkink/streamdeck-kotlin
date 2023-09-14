package nl.paulharkink.streamdeckapi

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper

data class Info(
    val application: Application,
    val plugin: Plugin,
    val devicePixelRatio: Int,
    val colors: Colors,
    val devices: List<DeviceInfo>
)

enum class Platform {
    @JsonProperty("win")
    WINDOWS,

    @JsonProperty("mac")
    MAC
}

data class Application(
    val font: String?,
    val language: String?,
    val platform: Platform,
    val platformVersion: String,
    val version: String
)

data class Plugin(val uuid: String, val version: String)

data class Colors(
    val buttonPressedBackgroundColor: ColorCode?,
    val buttonPressedBorderColor: ColorCode?,
    val buttonPressedTextColor: ColorCode?,
    val disabledColor: ColorCode?,
    val highlightColor: ColorCode?,
    val mouseDownColor: ColorCode?
)

data class DeviceInfo(
    val id: String,
    val name: String,
    val size: DeviceDimensions,
    val type: DeviceType
)

data class DeviceDimensions(val columns: Int, val rows: Int)

data class Coordinates(val column: Int, val row: Int)

@JvmInline
value class ColorCode(val code: String) {
    init {
        require("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$".toRegex().matches(code)) { "Invalid color code" }
    }
}

@JvmInline
value class DeviceId(val id: String)

@JvmInline
value class Context(val id: String)

@JvmInline
value class Action(val id: String)

@JvmInline
value class AnyJson(private val content: Map<String, Any>) {

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun create(objectMapper: ObjectMapper, data: Any): AnyJson {
            return AnyJson(objectMapper.convertValue(data, Map::class.java) as Map<String, Any>)
        }
    }

    fun <T> asType(objectMapper: ObjectMapper, type: JavaType) {
        return objectMapper.convertValue(content, type)
    }
}

data class CoordinatePair(val first: Int, val second: Int) {

    @JsonValue
    fun toJson(): List<Int> = listOf(first, second)

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromJson(list: List<Int>): CoordinatePair {
            require(list.size == 2) { "List must contain exactly two elements" }
            return CoordinatePair(list[0], list[1])
        }
    }
}

enum class DeviceType(private val value: Int) {
    StreamDeck(0),
    StreamDeckMini(1),
    StreamDeckXL(2),
    StreamDeckMobile(3),
    CorsairGKeys(4),
    UNKNOWN(-1);

    @JsonValue
    fun toValue(): Int {
        return value
    }

    companion object {
        private val VALUES = values().associateBy(DeviceType::value)

        @JsonCreator
        @JvmStatic
        fun fromValue(value: Int) = VALUES[value] ?: UNKNOWN
    }
}