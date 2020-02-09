package hospitaldigitaltwins.prehmanagement.eventmanagement

import hospitaldigitaltwins.prehmanagement.missions.MissionInfo
import hospitaldigitaltwins.prehmanagement.missions.MissionModel
import hospitaldigitaltwins.prehmanagement.missions.MissionService

class EventService {

    private val missionServices: ArrayList<MissionService> = ArrayList()

    private var _eventInfo: EventInfo? = null

    val mission: List<MissionInfo>
        get() = missionServices.map { it.model.missionInfo }

    var eventInfo: EventInfo?
        get() {
            return _eventInfo?.copy()
        }
        set(value) {
            _eventInfo = value
        }

    fun addMission(missionInfo: MissionInfo): Int {
        var mission = MissionService(MissionModel(missionInfo))
        missionServices.add(mission)
        return missionServices.indexOf(mission)
    }
}