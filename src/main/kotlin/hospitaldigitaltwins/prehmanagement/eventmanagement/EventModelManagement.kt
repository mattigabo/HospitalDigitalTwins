package hospitaldigitaltwins.prehmanagement.eventmanagement

import hospitaldigitaltwins.prehmanagement.missions.MissionModel
import java.util.*


class EventManagement {
    fun addEventInfo(event: EventModel)
    fun getEventInfo(event: EventModel)
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

    }

    fun addMission()
}