package HospitalDigitalTwins.ontologies

import java.util.*

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */
interface VitalParameter<T> {
    val value: T
    val acquisitionTime: Date
}

data class RespiratoryTract(override val value: RespiratoryTractValue, override val acquisitionTime: Date) : VitalParameter<RespiratoryTractValue>

enum class RespiratoryTractValue {
    OPEN, CLOSED
}

data class BreathingRate(override val value: Int, override val acquisitionTime: Date) : VitalParameter<Int>

data class OutlyingSaturation(override val value: Int, override val acquisitionTime: Date) : VitalParameter<Int>

data class RightPhotoReactive(override val value: Boolean, override val acquisitionTime: Date) : VitalParameter<Boolean>

data class DiastolicPressure(override val value: Double, override val acquisitionTime: Date) : VitalParameter<Double>

data class SistolicPressure(override val value: Double, override val acquisitionTime: Date) : VitalParameter<Double>

data class Heartbeat(val rate, val typology: HearthBeatTypologies) : VitalParameter

enum class HearthBeatTypologies(val stringFormat: String) {
    RITHMIC("rithmic"),
    ARITHMIC("arithmic");

    override fun toString(): String {
        return this.stringFormat
    }
}

data class BloodPressure(override val value: Int, override val acquisitionTime: Date) : VitalParameter<Int>