package nl.paulharkink.streamdeckapi

import com.fasterxml.jackson.annotation.JsonProperty

data class Info(
    val application: Application,
    val plugin: Plugin,
    val devicePixelRatio: Int,
    val colors: Colors,
    val devices: List<Device>
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

data class Device(
    val id: String,
    val name: String,
    val size: DeviceDimensions,
    val type: Int
)

data class DeviceDimensions(val columns: Int, val rows: Int)

@JvmInline
value class ColorCode(private val code: String) {
    init {
        require("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$".toRegex().matches(code)) { "Invalid color code" }
    }
}