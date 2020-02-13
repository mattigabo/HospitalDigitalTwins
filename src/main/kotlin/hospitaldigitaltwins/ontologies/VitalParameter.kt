package hospitaldigitaltwins.ontologies

import java.util.*

interface VitalParameter<T> {
    val name: String
    val value: T
    val acquisitionTime: Date
}


object VitalParametersNames {
    const val PHOTO_REACTIVE = "photo reactive"
    const val PUPILS_DIAMETER = "pupils diameter"
    const val RESPIRATORY_TRACT = "respiratory tract"
    const val BREATHING_RATE = "breathing rate"
    const val OXYGEN_SATURATION = "oxygen saturation"
    const val HEARTBEAT_RATE = "heartbeat rate"
    const val HEARTBEAT_TYPE = "heartbeat type"
    const val BLOOD_PRESSURE = "blood pressure"
    const val CAP_REFILL_TIME = "capillary refill time"
    const val SKIN_COLOR = "skin color"
    const val EYE_OPENING = "eye opening"
    const val VERBAL_RESPONSE = "verbal response"
    const val MOTOR_RESPONSE = "motor response"
    const val LEFT_PUPIL = "left pupil"
    const val RIGHT_PUPIL = "right pupil"
    const val LEFT_PHOTO_REACTIVE = "left photo reactive"
    const val RIGHT_PHOTO_REACTIVE = "right photo reactive"
    const val TEMPERATURE = "temperature"

    fun asNameList(): List<String> {
        return arrayListOf(
            PHOTO_REACTIVE,
            PUPILS_DIAMETER,
            RESPIRATORY_TRACT,
            BREATHING_RATE,
            OXYGEN_SATURATION,
            HEARTBEAT_RATE,
            HEARTBEAT_TYPE,
            BLOOD_PRESSURE,
            CAP_REFILL_TIME,
            SKIN_COLOR,
            EYE_OPENING,
            VERBAL_RESPONSE,
            MOTOR_RESPONSE,
            LEFT_PUPIL,
            RIGHT_PUPIL,
            LEFT_PHOTO_REACTIVE,
            RIGHT_PHOTO_REACTIVE,
            TEMPERATURE
        )
    }
}