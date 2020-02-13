package hospitaldigitaltwins.prehmanagement.ontologies

import hospitaldigitaltwins.ontologies.BasicVitalParameters
import hospitaldigitaltwins.ontologies.VitalParameter
import hospitaldigitaltwins.ontologies.VitalParametersNames.BLOOD_PRESSURE
import hospitaldigitaltwins.ontologies.VitalParametersNames.CAP_REFILL_TIME
import hospitaldigitaltwins.ontologies.VitalParametersNames.SKIN_COLOR
import java.util.*

object PreHVitalParameters {

    enum class CapRefillTimeValues(val stringFormat: String) {
        NORMAL("normal"),
        AUGMENTED("augmented"),
        NONE("none");

        override fun toString(): String {
            return this.stringFormat
        }
    }

    data class CapRefillTime(
        override val value: CapRefillTimeValues,
        override val acquisitionTime: Date
    ) : VitalParameter<CapRefillTimeValues> {
        override val name: String = CAP_REFILL_TIME
    }

    enum class SkinColors(val stringFormat: String) {
        NORMAL("normal"),
        PALE("pale"),
        CYANOTIC("cyanotic");

        override fun toString(): String {
            return this.stringFormat
        }
    }

    data class Skin(
        override val value: SkinColors,
        override val acquisitionTime: Date
    ) : VitalParameter<SkinColors> {
        override val name: String = SKIN_COLOR
    }

    data class BloodPressure(
        override val value: Int,
        override val acquisitionTime: Date
    ) : VitalParameter<Int> {
        override val name: String = BLOOD_PRESSURE
    }

}

data class VitalParameters(
    val respiratoryTract: BasicVitalParameters.RespiratoryTract,
    val breathingRate: BasicVitalParameters.BreathingRate,
    val hearbeatRate: BasicVitalParameters.Heartbeat.Rate,
    val hearbeatTypology: BasicVitalParameters.Heartbeat.Typology,
    val eyes: BasicVitalParameters.Eyes,
    val temperature: BasicVitalParameters.Temperature,
    val bloodPressure: PreHVitalParameters.BloodPressure,
    val skin: PreHVitalParameters.Skin
)