package hospitaldigitaltwins.prehmanagement.missions

import hospitaldigitaltwins.ontologies.Patient
import hospitaldigitaltwins.prehmanagement.ontologies.TrackingStep
import java.util.*

/**
 * Created by Matteo Gabellini on 06/02/2020.
 */
data class MissionModel(var missionInfo: MissionInfo, var patient: Patient? = null)

data class MissionInfo(
    var medic: String,
    var returnCode: Int,
    var releaseWard: String,
    var trackingStep: ArrayList<TrackingStep>,
    var involvedVehicle: ArrayList<String>
)