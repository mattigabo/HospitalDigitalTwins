package hospitaldigitaltwins.prehmanagement.eventmanagement

import digitaltwinframework.coreimplementation.utils.eventbusutils.FailureCode
import digitaltwinframework.coreimplementation.utils.eventbusutils.JsonResponse
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages
import hospitaldigitaltwins.prehmanagement.missions.MissionInfo
import hospitaldigitaltwins.prehmanagement.missions.MissionModel
import hospitaldigitaltwins.prehmanagement.missions.MissionService
import io.vertx.core.Promise
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

class EventService {

    val missionServices: ArrayList<MissionService> = ArrayList()

    private var _eventInfo: EventInfo? = null

    val missions: List<MissionInfo>
        get() = missionServices.map { it.missionInfo }

    var eventInfo: EventInfo?
        get() {
            return _eventInfo?.copy()
        }
        set(value) {
            _eventInfo = value
        }

    fun addMission(missionInfo: MissionInfo): Promise<Int> {
        var result = Promise.promise<Int>()
        MissionService.createMission(MissionModel(missionServices.size, missionInfo)).future().onComplete {
            missionServices.add(it.result())
            result.complete(missionServices.indexOf(it.result()))
        }.onFailure {
            result.fail(it)
        }
        return result
    }

    fun registerEventBusConsumers(eb: EventBus) {
        eb.consumer<JsonObject>(EventOperationIds.GET_EVENT_INFO) { message ->
            this.eventInfo?.let {
                message.reply(JsonObject.mapFrom(it))
            } ?: message.fail(500, "EventInfo not inserted")
        }

        eb.consumer<JsonObject>(EventOperationIds.ADD_EVENT_INFO) { message ->
            try {
                this.eventInfo = message.body().mapTo(EventInfo::class.java)
                message.reply(JsonObject.mapFrom(JsonResponse(StandardMessages.OPERATION_EXECUTED_MESSAGE)))
            } catch (e: IllegalArgumentException) {
                println(e.message)
                val failureMessage = StandardMessages.JSON_MALFORMED_MESSAGE_PREFIX +
                        EventInfo::class.java.toString()
                message.fail(FailureCode.JSON_MALFORMED, failureMessage)
            }
        }

        eb.consumer<JsonObject>(EventOperationIds.GET_MISSIONS) { message ->
            this.missions.let {
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
                this.addMission(mission).future().onComplete {
                    this.missionServices.get(it.result()).registerEventBusConsumers(eb)
                    this.missionServices.get(it.result()).patient.registerEventBusConsumers(eb)
                    val response = JsonObject()
                    response.put("missionId", it.result())
                    message.reply(response)
                }.onFailure {
                    message.fail(FailureCode.PROBLEM_IN_MISSION_CREATION, it.toString())
                }
            } catch (e: IllegalArgumentException) {
                println(e.message)
                val failureMessage = StandardMessages.JSON_MALFORMED_MESSAGE_PREFIX +
                        MissionInfo::class.java.toString()
                message.fail(FailureCode.JSON_MALFORMED, failureMessage)
            }
        }
    }
}