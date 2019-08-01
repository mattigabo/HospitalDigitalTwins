package HospitalDigitalTwins.ontologies

import java.util.*

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */
interface VitalParameter<T> {
    val value: T
    val acquisitionTime: Date
}

object VitalParameters {
    data class RespiratoryTract(
        override val value: RespiratoryTract.Value,
        override val acquisitionTime: Date
    ) : VitalParameter<RespiratoryTract.Value> {
        enum class Value {
            OPEN, CLOSED
        }
    }

    data class BreathingRate(override val value: Int, override val acquisitionTime: Date) : VitalParameter<Int>

    data class OutlyingSaturation(override val value: Int, override val acquisitionTime: Date) : VitalParameter<Int>

    data class RightPhotoReactive(override val value: Boolean, override val acquisitionTime: Date) :
        VitalParameter<Boolean>

    data class DiastolicPressure(override val value: Double, override val acquisitionTime: Date) :
        VitalParameter<Double>

    data class SistolicPressure(override val value: Double, override val acquisitionTime: Date) : VitalParameter<Double>

    data class Heartbeat(val rate, val typology: Heartbeat.Typologies) : VitalParameter {

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

    data class CapRefillTime(
        override val value: CapRefilTimeValues,
        override val acquisitionTime: Date
    ) : VitalParameter<CapRefilTimeValues>

    enum class CapRefilTimeValues(val stringFormat: String) {
        NORMAL("normal"),
        AUGMENTED("augmented"),
        NONE("none");

        override fun toString(): String {
            return this.stringFormat
        }
    }

    data class Skin(
        override val value: Skin.Colors,
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