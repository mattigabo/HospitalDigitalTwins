package hospitaldigitaltwins.prehmanagement.eventmanagement

import hospitaldigitaltwins.prehmanagement.missions.MissionModel
import java.util.*

/**
 * Created by Matteo Gabellini on 06/02/2020.
 */
data class EventInfo(
    val callTime: Date,
    val dynamics: String,
    val address: String,
    val emergencyType: String,
    val dispacthCode: String,
    var missions: List<MissionModel>
)