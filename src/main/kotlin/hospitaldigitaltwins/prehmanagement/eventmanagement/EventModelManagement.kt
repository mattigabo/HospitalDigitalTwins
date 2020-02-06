package hospitaldigitaltwins.prehmanagement.eventmanagement

import hospitaldigitaltwins.prehmanagement.missions.MissionInfo
import hospitaldigitaltwins.prehmanagement.missions.MissionModel
import java.util.*


class EventManagement {
    val missions: List<MissionModel> = ArrayList<MissionModel>
    var eventInfo: EventInfo? = null

    fun startMission(missionInfo: MissionInfo): Int {
        var mission = MissionModel(missionInfo,)
        return
    }
}