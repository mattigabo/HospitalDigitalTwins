package hospitaldigitaltwins.prehmanagement.eventmanagement

import digitaltwinframework.coreimplementation.restmanagement.AbstractRestHandlers
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages.EMPTY_MESSAGE
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext

object EventOperationIds {
    const val ADD_EVENT_INFO = "addEventInfo"
    const val GET_EVENT_INFO = "getEventInfo"

    const val ADD_MISSION = "addMission"
    const val GET_MISSIONS = "getAllMissions"
}

object EventRestHandlers : AbstractRestHandlers() {

    val onAddEventInfo = Handler<RoutingContext> { routingContext ->
        val eventInfo = routingContext.bodyAsJson
        eb.request<JsonObject>(EventOperationIds.ADD_EVENT_INFO, eventInfo, responseCallBack(routingContext))
    }

    val onGetEventInfo = Handler<RoutingContext> { routingContext ->
        eb.request<JsonObject>(EventOperationIds.GET_EVENT_INFO, EMPTY_MESSAGE, responseCallBack(routingContext))
    }


    val onGetMissions = Handler<RoutingContext> { routingContext ->
        eb.request<JsonObject>(EventOperationIds.GET_MISSIONS, EMPTY_MESSAGE, responseCallBack(routingContext))
    }

    val onAddMission = Handler<RoutingContext> { routingContext ->
        val missionInfo = routingContext.bodyAsJson
        eb.request<JsonObject>(EventOperationIds.ADD_MISSION, missionInfo, responseCallBack(routingContext))
    }
}
