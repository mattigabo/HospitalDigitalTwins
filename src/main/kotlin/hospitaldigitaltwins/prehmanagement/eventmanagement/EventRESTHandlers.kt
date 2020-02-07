package hospitaldigitaltwins.prehmanagement.eventmanagement

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

object EventOperationIds {
    const val ADD_EVENT_INFO = "addEventInfo"
    const val GET_EVENT_INFO = "getEventInfo"

    const val ADD_MISSION = "addMission"
    const val GET_MISSIONS = "getAllMissions"
}

object EventRestHandlers {

    val onAddEventInfo = Handler<RoutingContext> { routingContext ->
        println("Add Event Info Request")
    }

    val onGetEventInfo = Handler<RoutingContext> { routingContext ->
        println("onGetEventInfo")
    }


    val onGetMissions = Handler<RoutingContext> { routingContext ->
        println("onGetMissions")
    }

    val onAddMission = Handler<RoutingContext> { routingContext ->
        println("onGetMissions")
    }
}
