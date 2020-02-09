package hospitaldigitaltwins.prehmanagement

import digitaltwinframework.coreimplementation.AbstractDigitalTwin
import digitaltwinframework.coreimplementation.utils.eventbusutils.FailureCode
import digitaltwinframework.coreimplementation.utils.eventbusutils.JsonResponse
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages
import hospitaldigitaltwins.prehmanagement.eventmanagement.EventInfo
import hospitaldigitaltwins.prehmanagement.eventmanagement.EventOperationIds
import hospitaldigitaltwins.prehmanagement.eventmanagement.EventService
import hospitaldigitaltwins.prehmanagement.missions.MissionInfo
import hospitaldigitaltwins.prehmanagement.missions.MissionOperationIds
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject


class PreHEvolutionController(val thisDT: AbstractDigitalTwin) : AbstractVerticle() {

    val eventService = EventService()

    var restRoutingAdapter = RESTRoutingAdapter(thisDT.runningEnv.vertx, thisDT.identifier.toString())

    override fun start() {
        super.start()
        registerCoreHandlerToEventBus(thisDT.runningEnv.eventBus)
        restRoutingAdapter.loadOpenApiSpec()
    }

    private fun registerCoreHandlerToEventBus(eb: EventBus) {
        registerEventInfoConsumers(eb)
    }

    private fun registerEventInfoConsumers(eb: EventBus) {
        eb.consumer<JsonObject>(EventOperationIds.GET_EVENT_INFO) { message ->
            eventService.eventInfo?.let {
                message.reply(JsonObject.mapFrom(it))
            } ?: message.fail(500, "EventInfo not inserted")
        }

        eb.consumer<JsonObject>(EventOperationIds.ADD_EVENT_INFO) { message ->
            try {
                eventService.eventInfo = message.body().mapTo(EventInfo::class.java)
                message.reply(JsonObject.mapFrom(JsonResponse(StandardMessages.OPERATION_EXECUTED_MESSAGE)))
            } catch (e: IllegalArgumentException) {
                println(e.message)
                val failureMessage = StandardMessages.JSON_MALFORMED_MESSAGE_PREFIX + EventInfo::class.java.toString()
                message.fail(FailureCode.JSON_MALFORMED, failureMessage)
            }
        }

        eb.consumer<JsonObject>(EventOperationIds.GET_MISSIONS) { message ->
            eventService.missions.let {
                val response = JsonArray(it.map { missionInfo -> JsonObject.mapFrom(missionInfo) })
                message.reply(response)
            }
        }

        eb.consumer<JsonObject>(EventOperationIds.ADD_MISSION) { message ->
            try {
                val mission = MissionInfo(
                        message.body().getString("medicName"),
                        message.body().getJsonArray("vehicles").list as ArrayList<String>
                )
                val missionId = eventService.addMission(mission)
                val response = JsonObject()
                response.put("missionId", missionId)
                message.reply(response)
            } catch (e: IllegalArgumentException) {
                println(e.message)
                val failureMessage = StandardMessages.JSON_MALFORMED_MESSAGE_PREFIX + MissionInfo::class.java.toString()
                message.fail(FailureCode.JSON_MALFORMED, failureMessage)
            }
        }
    }

    private fun registerMissionInfoConsumers(eb: EventBus) {
        eb.consumer<JsonObject>(MissionOperationIds.GET_MISSION) { message ->
            eventService.missions.let {
                message.reply(JsonObject.mapFrom(it))
            }
        }
    }
}