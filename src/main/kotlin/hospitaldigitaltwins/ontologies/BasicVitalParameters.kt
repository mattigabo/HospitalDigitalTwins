package hospitaldigitaltwins.ontologies

import java.util.*

object BasicVitalParameters {

    object EyeParameters {

        data class PhotoReactive(
            override val value: Boolean,
            override val acquisitionTime: Date
        ) : VitalParameter<Boolean>

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
        ) : VitalParameter<PupilsTypes>

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
        ) : VitalParameter<OpeningTypes>
    }

    data class Eye(
            val PhotoReactive: EyeParameters.PhotoReactive,
            val Pupils: EyeParameters.Pupils,
            val Opening: EyeParameters.OpeningTypes
    )

    data class Eyes(val right: Eye, val left: Eye)

    data class RespiratoryTract(
        override val value: Value,
        override val acquisitionTime: Date
    ) : VitalParameter<RespiratoryTract.Value> {
        enum class Value {
            OPEN, CLOSED
        }
    }

    data class BreathingRate(override val value: Int, override val acquisitionTime: Date) :
        VitalParameter<Int>

    data class OutlyingSaturation(override val value: Int, override val acquisitionTime: Date) :
        VitalParameter<Int>

    object Heartbeat {

        data class Rate(override val value: Integer, override val acquisitionTime: Date) : VitalParameter<Integer>

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
        ) : VitalParameter<Heartbeat.Typologies>
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
    ) : VitalParameter<VerbalResponseTypes>

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
    ) : VitalParameter<MotorResponseTypes>

    data class TemperatureInCelsius(
            override val value: Double,
        override val acquisitionTime: Date
    ) : VitalParameter<Double>
}