rootProject.name = "StreamDeck Kotlin Plugin"
include("streamdeck-plugin")
include("streamdeck-plugin-backend")

project(":streamdeck-plugin").name = "StreamDeck Main Plugin"
project(":streamdeck-plugin-backend").name = "StreamDeck Plugin Kotlin Backend"
