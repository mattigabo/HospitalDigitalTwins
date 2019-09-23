package hospitaldigitaltwins.ontologies

import java.util.*

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */
interface VitalParameter<T> {
    val value: T
    val acquisitionTime: Date
}

object VitalParameters {

    object Eye {

        data class RightPhotoReactive(
            override val value: Boolean,
            override val acquisitionTime: Date
        ) : VitalParameter<Boolean>

        data class LeftPhotoReactive(
            override val value: Boolean,
            override val acquisitionTime: Date
        ) : VitalParameter<Boolean>
    }

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

    data class DiastolicPressure(override val value: Double, override val acquisitionTime: Date) :
        VitalParameter<Double>

    data class SistolicPressure(override val value: Double, override val acquisitionTime: Date) :
        VitalParameter<Double>

    object Heartbeat {

        data class Rate(override val value: Integer, override val acquisitionTime: Date) : VitalParameter<Integer>

        data class Typology(
            override val value: Heartbeat.Typologies,
            override val acquisitionTime: Date
        ) : VitalParameter<Heartbeat.Typologies>

        enum class Typologies(val stringFormat: String) {
            RITHMIC("rithmic"),
            ARITHMIC("arithmic");

            override fun toString(): String {
                return this.stringFormat
            }
        }

    }

    data class BloodPressure(
        override val value: Int,
        override val acquisitionTime: Date
    ) : VitalParameter<Int>

    class CapRefillTime(
        override val value: CapRefillTime.Values,
        override val acquisitionTime: Date
    ) : VitalParameter<CapRefillTime.Values> {

        enum class Values(val stringFormat: String) {
            NORMAL("normal"),
            AUGMENTED("augmented"),
            NONE("none");

            override fun toString(): String {
                return this.stringFormat
            }
        }
    }

    class Skin(
        override val value: Colors,
        override val acquisitionTime: Date
    ) : VitalParameter<Skin.Colors> {
        enum class Colors(val stringFormat: String) {
            NORMAL("normal"),
            PALE("pale"),
            CYANOTIC("cyanotic");

            override fun toString(): String {
                return this.stringFormat
            }
        }
    }
}