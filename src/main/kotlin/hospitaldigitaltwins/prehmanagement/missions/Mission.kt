package hospitaldigitaltwins.prehmanagement.missions

import hospitaldigitaltwins.prehmanagement.ontologies.TrackingStep
import java.util.*

/**
 * Created by Matteo Gabellini on 06/02/2020.
 */
data class MissionModel(var missionInfo: MissionInfo)

data class MissionInfo(
    var medic: String,
    var returnCode: Int,
    var releaseWard: String,
    var trackingStep: ArrayList<TrackingStep>,
    var involvedVehicle: ArrayList<String>
)