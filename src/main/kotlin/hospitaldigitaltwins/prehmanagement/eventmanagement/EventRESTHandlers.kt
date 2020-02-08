package hospitaldigitaltwins.prehmanagement.eventmanagement

import digitaltwinframework.coreimplementation.restmanagement.RESTDefaultResponse.sendServerErrorResponse
import digitaltwinframework.coreimplementation.restmanagement.RESTDefaultResponse.sendSuccessResponse
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages.EMPTY_MESSAGE
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext

object EventOperationIds {
    const val ADD_EVENT_INFO = "addEventInfo"
    const val GET_EVENT_INFO = "getEventInfo"

    const val ADD_MISSION = "addMission"
    const val GET_MISSIONS = "getAllMissions"
}

object EventRestHandlers {
    val eb = Vertx.currentContext().owner().eventBus()

    val onAddEventInfo = Handler<RoutingContext> { routingContext ->
        val eventInfo = routingContext.bodyAsJson
        eb.request<JsonObject>(EventOperationIds.ADD_EVENT_INFO, eventInfo) { ar ->
            when {
                ar.succeeded() -> sendSuccessResponse(ar.result().body().toString(), routingContext)
                ar.failed() -> sendServerErrorResponse(routingContext, ar.cause().message!!.toString())
                else -> sendServerErrorResponse(routingContext)
            }
        }
    }

    val onGetEventInfo = Handler<RoutingContext> { routingContext ->
        eb.request<JsonObject>(EventOperationIds.GET_EVENT_INFO, EMPTY_MESSAGE) { ar ->
            when {
                ar.succeeded() -> sendSuccessResponse(ar.result().body().toString(), routingContext)
                ar.failed() -> sendServerErrorResponse(routingContext, ar.cause().message!!.toString())
                else -> sendServerErrorResponse(routingContext)
            }
        }
    }


    val onGetMissions = Handler<RoutingContext> { routingContext ->
        println("onGetMissions")
    }

    val onAddMission = Handler<RoutingContext> { routingContext ->
        println("onAddMissions")
    }
}
