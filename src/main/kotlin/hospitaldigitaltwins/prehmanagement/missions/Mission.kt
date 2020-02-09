package hospitaldigitaltwins.prehmanagement.missions

import com.fasterxml.jackson.annotation.JsonProperty
import hospitaldigitaltwins.ontologies.Patient
import hospitaldigitaltwins.prehmanagement.ontologies.TrackingStep

/**
 * Created by Matteo Gabellini on 06/02/2020.
 */
data class MissionModel(var missionInfo: MissionInfo, var patient: Patient? = null)

data class MissionReturnInformation(
        @JsonProperty("returnCode") var returnCode: Int = 0,
        @JsonProperty("releaseWard") var releaseWard: String = "",
        @JsonProperty("hospital") var hospital: String = ""
)

data class MissionInfo(
        @JsonProperty("medic") var medic: String,
        @JsonProperty("involvedVehicle") var involvedVehicle: ArrayList<String> = ArrayList(),
        @JsonProperty("returnInfo") var returnInfo: MissionReturnInformation = MissionReturnInformation(),
        @JsonProperty("trackingStep") var trackingStep: ArrayList<TrackingStep> = ArrayList()
)