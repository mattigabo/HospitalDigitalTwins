package hospitaldigitaltwins.prehmanagement.eventmanagement

import hospitaldigitaltwins.prehmanagement.missions.MissionModel
import java.util.*


class EventManagement {
    var eventInfo: EventInfo? = null
    val missions: List<MissionModel> = ArrayList<MissionModel>

    fun addEventInfo(event: EventInfo) {

    }

    fun getEventInfo(event: EventInfo): EventInfo? {
        return eventInfo
    }

    fun startMission(): Int {

    }
}

class EventInfo(
    val callTime: Date,
    val dynamics: String,
    val address: String,
    val emergencyType: String,
    val dispacthCode: String,
    var missions: List<MissionModel>
) {
    fun involvedVeicle(): Int {
        missions
    }
}