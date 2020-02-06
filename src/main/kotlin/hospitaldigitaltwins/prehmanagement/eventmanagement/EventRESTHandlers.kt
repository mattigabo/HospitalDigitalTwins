package hospitaldigitaltwins.prehmanagement.eventmanagement

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

object EventOperationIds {
    const val POST_EVENT_INFO = "addEventInfo"
    const val GET_EVENT_INFO = "getEventInfo"

    const val ADD_MISSION = "addMission"
    const val GET_MISSIONS = "getAllMissions"
}

object EventRestHandlers {
    val onAddEventInfo = Handler<RoutingContext> { routingContext ->

    }

    val onGetEventInfo = Handler<RoutingContext> { routingContext ->

    }


    val onGetMissions = Handler<RoutingContext> { routingContext ->

    }

    val onAddMission = Handler<RoutingContext> { routingContext ->


    }
}
