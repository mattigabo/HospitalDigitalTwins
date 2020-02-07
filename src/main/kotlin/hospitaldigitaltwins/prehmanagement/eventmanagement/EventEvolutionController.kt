package hospitaldigitaltwins.prehmanagement.eventmanagement

import digitaltwinframework.coreimplementation.AbstractDigitalTwin
import hospitaldigitaltwins.prehmanagement.missions.MissionInfo
import hospitaldigitaltwins.prehmanagement.missions.MissionManager
import hospitaldigitaltwins.prehmanagement.missions.MissionModel
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus


class EventEvolutionController(val thisDT: AbstractDigitalTwin) : AbstractVerticle() {
    val missions: ArrayList<MissionManager> = ArrayList<MissionManager>()
    var eventInfo: EventInfo? = null

    var restRoutingAdapter = RESTRoutingAdapter(thisDT.runningEnv.vertx, thisDT.identifier.toString())

    fun startMission(missionInfo: MissionInfo): Int {
        var mission = MissionManager(MissionModel(missionInfo))
        missions.add(mission)
        return missions.indexOf(mission)
    }

    override fun start() {
        super.start()
        restRoutingAdapter.loadOpenApiSpec()
    }

    private fun registerCoreHandlerToEventBus(eb: EventBus) {

    }

}