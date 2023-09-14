package nl.paulharkink.streamdeckapi.ws

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import nl.paulharkink.streamdeckapi.*

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "event"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = DidReceiveSettings::class, name = DidReceiveSettings.ID),
    JsonSubTypes.Type(value = DidReceiveGlobalSettings::class, name = DidReceiveGlobalSettings.ID),
    JsonSubTypes.Type(value = TouchTap::class, name = TouchTap.ID),
    JsonSubTypes.Type(value = DialDown::class, name = DialDown.ID),
    JsonSubTypes.Type(value = DialUp::class, name = DialUp.ID),
    JsonSubTypes.Type(value = DialPress::class, name = DialPress.ID),
    JsonSubTypes.Type(value = DialRotate::class, name = DialRotate.ID),
    JsonSubTypes.Type(value = KeyDown::class, name = KeyDown.ID),
    JsonSubTypes.Type(value = KeyUp::class, name = KeyUp.ID),
    JsonSubTypes.Type(value = WillAppear::class, name = WillAppear.ID),
    JsonSubTypes.Type(value = WillDisappear::class, name = WillDisappear.ID),
    JsonSubTypes.Type(value = TitleParametersDidChange::class, name = TitleParametersDidChange.ID),
    JsonSubTypes.Type(value = DeviceDidConnect::class, name = DeviceDidConnect.ID),
    JsonSubTypes.Type(value = DeviceDidDisconnect::class, name = DeviceDidDisconnect.ID),
    JsonSubTypes.Type(value = ApplicationDidLaunch::class, name = ApplicationDidLaunch.ID),
    JsonSubTypes.Type(value = ApplicationDidTerminate::class, name = ApplicationDidTerminate.ID),
    JsonSubTypes.Type(value = SystemDidWakeUp::class, name = SystemDidWakeUp.ID),
    JsonSubTypes.Type(value = PropertyInspectorDidAppear::class, name = PropertyInspectorDidAppear.ID),
    JsonSubTypes.Type(value = PropertyInspectorDidDisappear::class, name = PropertyInspectorDidDisappear.ID),
    JsonSubTypes.Type(value = SendToPlugin::class, name = SendToPlugin.ID),
    JsonSubTypes.Type(value = SendToPropertyInspector::class, name = SendToPropertyInspector.ID)
)
sealed interface ReceivedEvent {
    val event: String
}


data class DidReceiveSettings(
    val action: Action,
    override val event: String = ID,
    val context: Context,
    val device: DeviceId,
    val payload: Payload
) : ReceivedEvent {
    companion object {
        const val ID = "didReceiveSettings"
    }

    data class Payload(
        val settings: AnyJson,
        val coordinates: Coordinates,
        val isInMultiAction: Boolean,
        val state: Int? = null
    )
}

data class DidReceiveGlobalSettings(
    override val event: String = ID,
    val payload: Payload
) : ReceivedEvent {
    companion object {
        const val ID = "didReceiveGlobalSettings"
    }

    data class Payload(
        val settings: AnyJson
    )
}

data class TouchTap(
    val action: Action,
    override val event: String = ID,
    val context: Context,
    val device: DeviceId,
    val payload: Payload
) : ReceivedEvent {
    companion object {
        const val ID = "touchTap"
    }

    data class Payload(
        val settings: AnyJson,
        val coordinates: Coordinates,
        val tapPos: CoordinatePair,
        val hold: Boolean
    )
}

data class DialDown(
    val action: Action,
    override val event: String = ID,
    val context: Context,
    val device: DeviceId,
    val payload: Payload
) : ReceivedEvent {
    companion object {
        const val ID = "dialDown"
    }

    data class Payload(
        val settings: AnyJson,
        val coordinates: Coordinates,
        val controller: String
    )
}

data class DialUp(
    val action: Action,
    override val event: String = ID,
    val context: Context,
    val device: DeviceId,
    val payload: Payload
) : ReceivedEvent {
    companion object {
        const val ID = "dialUp"
    }

    data class Payload(
        val settings: AnyJson,
        val coordinates: Coordinates,
        val controller: String
    )
}

data class DialPress(
    val action: Action,
    override val event: String = ID,
    val context: Context,
    val device: DeviceId,
    val payload: Payload
) : ReceivedEvent {
    companion object {
        const val ID = "dialPress"
    }

    data class Payload(
        val settings: AnyJson,
        val coordinates: Coordinates,
        val pressed: Boolean
    )
}

data class DialRotate(
    val action: Action,
    override val event: String = ID,
    val context: Context,
    val device: DeviceId,
    val payload: Payload
) : ReceivedEvent {
    companion object {
        const val ID = "dialRotate"
    }

    data class Payload(
        val settings: AnyJson,
        val coordinates: Coordinates,
        val ticks: Int,
        val pressed: Boolean
    )
}

data class KeyDown(
    val action: Action,
    override val event: String = ID,
    val context: Context,
    val device: DeviceId,
    val payload: Payload
) : ReceivedEvent {
    companion object {
        const val ID = "keyDown"
    }

    data class Payload(
        val settings: AnyJson,
        val coordinates: Coordinates,
        val state: Int? = null,
        val userDesiredState: Int? = null,
        val isInMultiAction: Boolean
    )
}

data class KeyUp(
    val action: Action,
    override val event: String = ID,
    val context: Context,
    val device: DeviceId,
    val payload: Payload
) : ReceivedEvent {
    companion object {
        const val ID = "keyUp"
    }

    data class Payload(
        val settings: AnyJson,
        val coordinates: Coordinates,
        val state: Int? = null,
        val userDesiredState: Int? = null,
        val isInMultiAction: Boolean
    )
}

data class WillAppear(
    val action: Action,
    override val event: String = ID,
    val context: Context,
    val device: DeviceId,
    val payload: Payload
) : ReceivedEvent {
    companion object {
        const val ID = "willAppear"
    }

    data class Payload(
        val settings: AnyJson,
        val coordinates: Coordinates,
        val state: Int? = null,
        val isInMultiAction: Boolean,
        val controller: String? = null
    )
}

data class WillDisappear(
    val action: Action,
    override val event: String = ID,
    val context: Context,
    val device: DeviceId,
    val payload: Payload
) : ReceivedEvent {
    companion object {
        const val ID = "willDisappear"
    }

    data class Payload(
        val settings: AnyJson,
        val coordinates: Coordinates,
        val state: Int? = null,
        val isInMultiAction: Boolean
    )
}

data class TitleParametersDidChange(
    val action: Action,
    override val event: String = ID,
    val context: Context,
    val device: DeviceId,
    val payload: Payload
) : ReceivedEvent {
    companion object {
        const val ID = "titleParametersDidChange"
    }

    data class Payload(
        val settings: AnyJson,
        val coordinates: Coordinates,
        val state: Int? = null,
        val title: String,
        val titleParameters: TitleParameters
    )

    data class TitleParameters(
        val fontFamily: String,
        val fontSize: Int,
        val fontStyle: String,
        val fontUnderline: Boolean,
        val showTitle: Boolean,
        val titleAlignment: String,
        val titleColor: ColorCode
    )
}

data class DeviceDidConnect(
    override val event: String = ID,
    val device: DeviceId,
    val deviceInfo: DeviceInfo
) : ReceivedEvent {
    companion object {
        const val ID = "deviceDidConnect"
    }
}

data class DeviceDidDisconnect(
    override val event: String = ID,
    val device: DeviceId,
) : ReceivedEvent {
    companion object {
        const val ID = "deviceDidDisconnect"
    }
}

data class ApplicationDidLaunch(
    override val event: String = ID,
    val payload: Payload,
) : ReceivedEvent {
    companion object {
        const val ID = "applicationDidLaunch"
    }

    data class Payload(val application: String)
}

data class ApplicationDidTerminate(
    override val event: String = ID,
    val payload: Payload,
) : ReceivedEvent {
    companion object {
        const val ID = "applicationDidTerminate"
    }

    data class Payload(val application: String)
}

data class SystemDidWakeUp(
    @field:JsonProperty("event")
    override val event: String = ID,
) : ReceivedEvent {
    companion object {
        const val ID = "systemDidWakeUp"
    }
}

data class PropertyInspectorDidAppear(
    val action: Action,
    override val event: String = ID,
    val context: Context,
    val device: DeviceId
) : ReceivedEvent {
    companion object {
        const val ID = "propertyInspectorDidAppear"
    }
}

data class PropertyInspectorDidDisappear(
    val action: Action,
    override val event: String = ID,
    val context: Context,
    val device: DeviceId
) : ReceivedEvent {
    companion object {
        const val ID = "propertyInspectorDidDisappear"
    }
}

data class SendToPlugin(
    val action: Action,
    override val event: String = ID,
    val context: Context,
    val payload: AnyJson
) : ReceivedEvent {
    companion object {
        const val ID = "sendToPlugin"
    }
}

data class SendToPropertyInspector(
    val action: Action,
    override val event: String = ID,
    val context: Context,
    val payload: AnyJson
) : ReceivedEvent {
    companion object {
        const val ID = "sendToPropertyInspector"
    }
}