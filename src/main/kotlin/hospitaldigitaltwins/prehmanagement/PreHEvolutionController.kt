package hospitaldigitaltwins.prehmanagement

import digitaltwinframework.coreimplementation.AbstractDigitalTwin
import digitaltwinframework.coreimplementation.utils.eventbusutils.FailureCode
import digitaltwinframework.coreimplementation.utils.eventbusutils.JsonResponse
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages
import hospitaldigitaltwins.prehmanagement.eventmanagement.EventInfo
import hospitaldigitaltwins.prehmanagement.eventmanagement.EventOperationIds
import hospitaldigitaltwins.prehmanagement.eventmanagement.EventService
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
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
        eb.consumer<JsonObject>(EventOperationIds.GET_EVENT_INFO) { message ->
            eventService.eventInfo?.let {
                message.reply(JsonObject.mapFrom(it))
            } ?: message.fail(500, "EventInfo not inserted")
        }

        eb.consumer<JsonObject>(EventOperationIds.ADD_EVENT_INFO) {
            try {
                eventService.eventInfo = it.body().mapTo(EventInfo::class.java)
                it.reply(JsonObject.mapFrom(JsonResponse(StandardMessages.OPERATION_EXECUTED_MESSAGE)))
            } catch (e: IllegalArgumentException) {
                println(e.message)
                val failureMessage = StandardMessages.JSON_MALFORMED_MESSAGE_PREFIX + EventInfo::class.java.toString()
                it.fail(FailureCode.JSON_MALFORMED, failureMessage)
            }
        }
    }


}