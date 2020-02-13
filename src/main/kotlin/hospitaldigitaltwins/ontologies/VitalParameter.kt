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
}