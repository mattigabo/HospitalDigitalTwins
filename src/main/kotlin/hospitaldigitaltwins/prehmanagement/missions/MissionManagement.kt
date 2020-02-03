package hospitaldigitaltwins.prehmanagement.missions

import hospitaldigitaltwins.prehmanagement.ontologies.TrackingStep
import java.util.*

data class MissionModel(
    val medic: String,
    var returnCode: Int,
    var releaseWard: String,
    var trackingStep: ArrayList<TrackingStep>
)


class MissionManagement
