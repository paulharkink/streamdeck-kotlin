rootProject.name = "StreamDeck_Kotlin_Plugin"
include("streamdeck-plugin")
include("streamdeck-plugin-backend")

project(":streamdeck-plugin").name = "StreamDeck_Main_Plugin"
project(":streamdeck-plugin-backend").name = "StreamDeck_Plugin_Kotlin_Backend"
