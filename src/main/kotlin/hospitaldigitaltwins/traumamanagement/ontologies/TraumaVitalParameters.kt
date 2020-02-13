package hospitaldigitaltwins.traumamanagement.ontologies

import hospitaldigitaltwins.ontologies.BasicVitalParameters
import hospitaldigitaltwins.ontologies.VitalParameter
import hospitaldigitaltwins.traumamanagement.ontologies.TraumaVitalParametersName.DISTOLIC_PRESSURE
import hospitaldigitaltwins.traumamanagement.ontologies.TraumaVitalParametersName.END_TIDAL_CARBON_DIOXIDE
import hospitaldigitaltwins.traumamanagement.ontologies.TraumaVitalParametersName.SYSTOLIC_PRESSURE
import java.util.*

object TraumaVitalParamenters {
    data class DiastolicPressure(
        override val value: Double,
        override val acquisitionTime: Date
    ) : VitalParameter<Double> {
        override val name: String = DISTOLIC_PRESSURE
    }

    data class SystolicPressure(
        override val value: Double,
        override val acquisitionTime: Date
    ) : VitalParameter<Double> {
        override val name: String = SYSTOLIC_PRESSURE
    }

    data class EndTidalCarbonDioxide(
        override val value: Integer,
        override val acquisitionTime: Date
    ) : VitalParameter<Integer> {
        override val name: String = END_TIDAL_CARBON_DIOXIDE
    }
}

object TraumaVitalParametersName {
    const val DISTOLIC_PRESSURE = "distolic pressure"
    const val SYSTOLIC_PRESSURE = "systolic pressure"
    const val END_TIDAL_CARBON_DIOXIDE = "end tidal carbon dioxide"
}

data class VitalParameters(
    val respiratoryTract: BasicVitalParameters.RespiratoryTract,
    val breathingRate: BasicVitalParameters.BreathingRate,
    val hearbeatRate: BasicVitalParameters.Heartbeat.Rate,
    val hearbeatTypology: BasicVitalParameters.Heartbeat.Typology,
    val eyes: BasicVitalParameters.Eyes,
    val temperature: BasicVitalParameters.Temperature,
    val endTidalCarbonDioxide: TraumaVitalParamenters.EndTidalCarbonDioxide
)