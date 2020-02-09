package hospitaldigitaltwins.prehmanagement.missions

import hospitaldigitaltwins.prehmanagement.ontologies.MissionSteps
import hospitaldigitaltwins.prehmanagement.ontologies.TrackingStep
import java.time.LocalDateTime


class MissionService(private var model: MissionModel) {
    val missionInfo: MissionInfo
        get() = model.missionInfo
    var medic: String
        get() = model.missionInfo.medic
        set(value) {
            model.missionInfo.medic = value
        }
    var retutnInfo: MissionReturnInformation
        get() = model.missionInfo.returnInfo
        set(value) {
            model.missionInfo.returnInfo = value
        }
    val tracking: List<TrackingStep>
        get() = model.missionInfo.trackingStep

    fun onDepartureFromHospital() {
        model.missionInfo.trackingStep.add(MissionSteps.DEPATURE_FROM_HOSPITAL.occurs(LocalDateTime.now()))
    }

    fun onArrivalOnSite() {
        model.missionInfo.trackingStep.add(MissionSteps.ARRIVAL_ON_SITE.occurs(LocalDateTime.now()))
    }

    fun onDepartureFromSite() {
        model.missionInfo.trackingStep.add(MissionSteps.DEPARTURE_FROM_SITE.occurs(LocalDateTime.now()))
    }

    fun onArrivalAtTheHospital() {
        model.missionInfo.trackingStep.add(MissionSteps.ARRIVAL_IN_HOSPITAL.occurs(LocalDateTime.now()))
    }
}
