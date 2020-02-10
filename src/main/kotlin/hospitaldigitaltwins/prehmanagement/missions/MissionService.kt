package hospitaldigitaltwins.prehmanagement.missions

import digitaltwinframework.coreimplementation.utils.eventbusutils.FailureCode
import digitaltwinframework.coreimplementation.utils.eventbusutils.JsonResponse
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages
import hospitaldigitaltwins.prehmanagement.ontologies.MissionSteps
import hospitaldigitaltwins.prehmanagement.ontologies.TrackingStep
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
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

    fun registerEventBusConsumers(eb: EventBus, missionId: Int) {
        eb.consumer<JsonObject>(MissionOperationIds.GET_MISSION + missionId) { message ->
            message.reply(JsonObject.mapFrom(this.missionInfo))
        }

        eb.consumer<JsonObject>(MissionOperationIds.PUT_MEDIC + missionId) { message ->
            try {
                this.medic = message.body().getString("medicName")
                message.reply(JsonObject.mapFrom(JsonResponse(StandardMessages.OPERATION_EXECUTED_MESSAGE)))
            } catch (e: IllegalStateException) {
                println(e)
                val failureMessage = "The Json passed as parameter not contains the field 'medicName'"
                message.fail(FailureCode.JSON_MALFORMED, failureMessage)
            }
        }

        eb.consumer<JsonObject>(MissionOperationIds.GET_MEDIC + missionId) { message ->
            val response = JsonObject()
            response.put("medicName", this.medic)
            message.reply(response)
        }

        eb.consumer<JsonObject>(MissionOperationIds.PUT_RETURN_INFO + missionId) { message ->
            try {
                this.retutnInfo = message.body().mapTo(MissionReturnInformation::class.java)
                message.reply(JsonObject.mapFrom(JsonResponse(StandardMessages.OPERATION_EXECUTED_MESSAGE)))
            } catch (e: IllegalArgumentException) {
                println(e.message)
                val failureMessage =
                    StandardMessages.JSON_MALFORMED_MESSAGE_PREFIX + MissionReturnInformation::class.java.toString()
                message.fail(FailureCode.JSON_MALFORMED, failureMessage)
            }
        }

        eb.consumer<JsonObject>(MissionOperationIds.GET_RETURN_INFO + missionId) { message ->
            message.reply(JsonObject.mapFrom(this.retutnInfo))
        }

        eb.consumer<JsonObject>(MissionOperationIds.GET_TRACKING + missionId) { message ->
            message.reply(JsonArray(this.tracking.map { JsonObject.mapFrom(it) }))
        }

        eb.consumer<JsonObject>(MissionOperationIds.DEPARTURE_FROM_HOSPITAL + missionId) { message ->
            this.onDepartureFromHospital()
            message.reply(JsonObject.mapFrom(JsonResponse(StandardMessages.OPERATION_EXECUTED_MESSAGE)))
        }

        eb.consumer<JsonObject>(MissionOperationIds.ARRIVAL_ON_SITE + missionId) { message ->
            this.onArrivalOnSite()
            message.reply(JsonObject.mapFrom(JsonResponse(StandardMessages.OPERATION_EXECUTED_MESSAGE)))
        }

        eb.consumer<JsonObject>(MissionOperationIds.DEPARTURE_FROM_SITE + missionId) { message ->
            this.onDepartureFromSite()
            message.reply(JsonObject.mapFrom(JsonResponse(StandardMessages.OPERATION_EXECUTED_MESSAGE)))
        }

        eb.consumer<JsonObject>(MissionOperationIds.ARRIVAL_AT_THE_HOSPITAL + missionId) { message ->
            this.onArrivalAtTheHospital()
            message.reply(JsonObject.mapFrom(JsonResponse(StandardMessages.OPERATION_EXECUTED_MESSAGE)))
        }
    }
}
