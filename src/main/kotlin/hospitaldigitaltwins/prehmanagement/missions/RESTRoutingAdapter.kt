package hospitaldigitaltwins.prehmanagement.missions

import digitaltwinframework.coreimplementation.restmanagement.AbstractRESTInteractionAdapter

class RESTRoutingAdapter : AbstractRESTInteractionAdapter

object MissionOperationIDS {
    const val ADD_MISSION = "addMission"
    const val GET_MISSIONS = "getAllMissions"

    const val GET_MISSION = "getMission"
    const val DEL_MISSION = "deleteMission"

    const val PUT_ONGOING = "putMissionOngoing"
    const val GET_ONGOING = "getMissionOngoing"

    const val PUT_MEDIC = "addMissionMedic"
    const val GET_MEDIC = "getMissionMedic"
    const val DEL_MEDIC = "delMissionMedic"

    const val PUT_RETURN_INFO = "putMissionReturnInfo"
    const val GET_RETURN_INFO = "getMissionReturnInfo"
    const val DEL_RETURN_INFO = "delMissionReturnInfo"

    const val GET_TRACKING = "getMissionTrackingInfo"
    const val PUT_TRACKING_STEP = "putMissionTrackingStep"
    const val GET_TRACKING_STEP = "getMissionTrackingStep"
    const val DEL_TRACKING_STEP = "delMissionTrackingStep"
}