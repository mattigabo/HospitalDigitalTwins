package hospitaldigitaltwins.prehmanagement.ontologies

import hospitaldigitaltwins.ontologies.BasicVitalParameters
import hospitaldigitaltwins.ontologies.VitalParameter
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
    ) : VitalParameter<CapRefillTimeValues>

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
    ) : VitalParameter<SkinColors>

    data class BloodPressure(
            override val value: Int,
            override val acquisitionTime: Date
    ) : VitalParameter<Int>

}

data class VitalParameters(
        val respiratoryTract: BasicVitalParameters.RespiratoryTract,
        val breathingRate: BasicVitalParameters.BreathingRate,
        val outlyingSaturation: BasicVitalParameters.OutlyingSaturation,
        val hearbeatRate: BasicVitalParameters.Heartbeat.Rate,
        val hearbeatTypology: BasicVitalParameters.Heartbeat.Typology,
        val eyes: BasicVitalParameters.Eyes,
        val temperatureInCelsius: BasicVitalParameters.TemperatureInCelsius,
        val bloodPressure: PreHVitalParameters.BloodPressure,
        val skin: PreHVitalParameters.Skin
)