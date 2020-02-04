package hospitaldigitaltwins.prehmanagement.missions

import digitaltwinframework.coreimplementation.restmanagement.AbstractRESTInteractionAdapter

class RESTRoutingAdapter : AbstractRESTInteractionAdapter

object MissionOperationIDS {
    const val ADD_MISSION = "addMission"
    const val GET_MISSIONS = "getAllMissions"

    const val GET_MISSION = "getMission"
    const val DEL_MISSION = "deleteMission"

    const val PUT_MEDIC = "setMissionMedic"
    const val GET_MEDIC = "getMissionMedic"

    const val PUT_RETURN_INFO = "setMissionReturnInfo"
    const val GET_RETURN_INFO = "getMissionReturnInfo"

    const val GET_TRACKING = "getMissionTrackingInfo"
    const val DEPARTURE_FROM_HOSPITAL = "departureFromHospital"
    const val ARRIVAL_ON_SITE = "arrivalOnSite"
    const val DEPARTURE_FROM_SITE = "departureFromSite"
    const val ARRIVAL_AT_THE_HOSPITAL = "arrivalAtTheHospital"
}