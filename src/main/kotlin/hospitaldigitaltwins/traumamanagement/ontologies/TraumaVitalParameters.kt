package hospitaldigitaltwins.traumamanagement.ontologies

import hospitaldigitaltwins.ontologies.BasicVitalParameters
import hospitaldigitaltwins.ontologies.VitalParameter
import java.util.*

object TraumaVitalParamenters {
    data class DiastolicPressure(override val value: Double, override val acquisitionTime: Date) :
            VitalParameter<Double>

    data class SystolicPressure(override val value: Double, override val acquisitionTime: Date) :
            VitalParameter<Double>

    data class OxygenSaturation(override val value: Integer, override val acquisitionTime: Date) :
            VitalParameter<Integer>

    data class EndTidalCarbonDioxide(override val value: Integer, override val acquisitionTime: Date) :
            VitalParameter<Integer>
}

data class VitalParameters(
        val respiratoryTract: BasicVitalParameters.RespiratoryTract,
        val breathingRate: BasicVitalParameters.BreathingRate,
        val outlyingSaturation: BasicVitalParameters.OutlyingSaturation,
        val hearbeatRate: BasicVitalParameters.Heartbeat.Rate,
        val hearbeatTypology: BasicVitalParameters.Heartbeat.Typology,
        val eyes: BasicVitalParameters.Eyes,
        val temperatureInCelsius: BasicVitalParameters.TemperatureInCelsius,
        val oxygenSaturation: TraumaVitalParamenters.OxygenSaturation,
        val endTidalCarbonDioxide: TraumaVitalParamenters.EndTidalCarbonDioxide
)