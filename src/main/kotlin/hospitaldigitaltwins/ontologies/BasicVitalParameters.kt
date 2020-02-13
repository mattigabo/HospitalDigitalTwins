package hospitaldigitaltwins.ontologies

import hospitaldigitaltwins.ontologies.VitalParametersNames.BREATHING_RATE
import hospitaldigitaltwins.ontologies.VitalParametersNames.EYE_OPENING
import hospitaldigitaltwins.ontologies.VitalParametersNames.HEARTBEAT_RATE
import hospitaldigitaltwins.ontologies.VitalParametersNames.HEARTBEAT_TYPE
import hospitaldigitaltwins.ontologies.VitalParametersNames.MOTOR_RESPONSE
import hospitaldigitaltwins.ontologies.VitalParametersNames.OXYGEN_SATURATION
import hospitaldigitaltwins.ontologies.VitalParametersNames.PHOTO_REACTIVE
import hospitaldigitaltwins.ontologies.VitalParametersNames.PUPILS_DIAMETER
import hospitaldigitaltwins.ontologies.VitalParametersNames.RESPIRATORY_TRACT
import hospitaldigitaltwins.ontologies.VitalParametersNames.TEMPERATURE
import hospitaldigitaltwins.ontologies.VitalParametersNames.VERBAL_RESPONSE
import java.util.*

object BasicVitalParameters {

    object EyeParameters {

        data class PhotoReactive(
            override val value: Boolean,
            override val acquisitionTime: Date
        ) : VitalParameter<Boolean> {
            override val name: String = PHOTO_REACTIVE
        }

        enum class PupilsTypes(private val stringFormat: String) {
            MIOSIS("miosis"),
            MYDRIASIS("mydriasis"),
            NORMAL("normal");

            override fun toString(): String {
                return this.stringFormat
            }
        }

        data class Pupils(
            override val value: PupilsTypes,
            override val acquisitionTime: Date
        ) : VitalParameter<PupilsTypes> {
            override val name: String = PUPILS_DIAMETER
        }

        enum class OpeningTypes(private val stringFormat: String) {
            SPONTANEOUS("4 - Spontaneous"),
            SOUND_STIMULI("3 - Sound stimuli"),
            PRESSURE_STIMULI("2 - Pressure stimuli"),
            NONE("1 - None"),
            NOT_DEFINABLE("ND - Not definable");

            override fun toString(): String {
                return this.stringFormat
            }
        }

        data class Opening(
            override val value: OpeningTypes,
            override val acquisitionTime: Date
        ) : VitalParameter<OpeningTypes> {
            override val name: String = EYE_OPENING
        }
    }

    data class Eye(
        val PhotoReactive: EyeParameters.PhotoReactive,
        val Pupils: EyeParameters.Pupils
    )

    data class Eyes(val right: Eye, val left: Eye)

    data class RespiratoryTract(
        override val value: Value,
        override val acquisitionTime: Date
    ) : VitalParameter<RespiratoryTract.Value> {
        enum class Value {
            OPEN, CLOSED
        }

        override val name: String = RESPIRATORY_TRACT
    }

    data class BreathingRate(
        override val value: Int,
        override val acquisitionTime: Date
    ) : VitalParameter<Int> {
        override val name: String = BREATHING_RATE
    }

    data class OxygenSaturation(
        override val value: Int,
        override val acquisitionTime: Date
    ) : VitalParameter<Int> {
        override val name: String = OXYGEN_SATURATION
    }

    object Heartbeat {

        data class Rate(override val value: Integer, override val acquisitionTime: Date) : VitalParameter<Integer> {
            override val name: String = HEARTBEAT_RATE
        }

        enum class Typologies(val stringFormat: String) {
            RITHMIC("rithmic"),
            ARITHMIC("arithmic");

            override fun toString(): String {
                return this.stringFormat
            }
        }

        data class Typology(
            override val value: Heartbeat.Typologies,
            override val acquisitionTime: Date
        ) : VitalParameter<Heartbeat.Typologies> {
            override val name: String = HEARTBEAT_TYPE
        }
    }

    enum class VerbalResponseTypes(val stringFormat: String) {
        ORIENTED("5 - Oriented"),
        CONFUSED("4 - Confused"),
        WORDS("3 - Words"),
        SOUNDS("2 - Sounds"),
        NONE("1 - None"),
        NOT_DEFINABLE("ND - Not definable");

        override fun toString(): String {
            return this.stringFormat
        }
    }

    data class VerbalResponse(
        override val value: VerbalResponseTypes,
        override val acquisitionTime: Date
    ) : VitalParameter<VerbalResponseTypes> {
        override val name: String = VERBAL_RESPONSE
    }


    enum class MotorResponseTypes(val stringFormat: String) {
        EXECUTES_ORDERS("6 - Executes orders"),
        LOCALIZES("5 - Localizes"),
        NORMAl_FLEXION("4 - Normal flexion"),
        ABNORMAL_FLEXION("3 - Abnormal flexion"),
        EXTENSION("2 - Extension"),
        NONE("1 - None"),
        NOT_DEFINABLE("ND - Not definable");

        override fun toString(): String {
            return this.stringFormat
        }
    }

    data class motorResponse(
        override val value: MotorResponseTypes,
        override val acquisitionTime: Date
    ) : VitalParameter<MotorResponseTypes> {
        override val name: String = MOTOR_RESPONSE
    }

    data class Temperature(
        override val value: Double,
        override val acquisitionTime: Date
    ) : VitalParameter<Double> {
        override val name: String = TEMPERATURE
    }
}