package hospitaldigitaltwins.prehmanagement.eventmanagement

import digitaltwinframework.coreimplementation.restmanagement.EventBusRestRequestForwarder
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages.EMPTY_MESSAGE
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext

object EventRestHandlers {

    val onAddEventInfo = Handler<RoutingContext> { routingContext ->
        val eventInfo = routingContext.bodyAsJson
        EventBusRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            EventOperationIds.ADD_EVENT_INFO,
            eventInfo
        )
    }

    val onGetEventInfo = Handler<RoutingContext> { routingContext ->
        EventBusRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            EventOperationIds.GET_EVENT_INFO,
            EMPTY_MESSAGE
        )
    }


    val onGetMissions = Handler<RoutingContext> { routingContext ->
        EventBusRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            EventOperationIds.GET_MISSIONS,
            EMPTY_MESSAGE
        )
    }

    val onAddMission = Handler<RoutingContext> { routingContext ->
        val missionInfo = routingContext.bodyAsJson
        EventBusRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            EventOperationIds.ADD_MISSION,
            missionInfo
        )
    }
}

object EventOperationIds {
    const val ADD_EVENT_INFO = "addEventInfo"
    const val GET_EVENT_INFO = "getEventInfo"

    const val ADD_MISSION = "addMission"
    const val GET_MISSIONS = "getAllMissions"
}