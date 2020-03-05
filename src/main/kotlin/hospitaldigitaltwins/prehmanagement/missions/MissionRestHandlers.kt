package hospitaldigitaltwins.prehmanagement.missions

import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages.EMPTY_MESSAGE
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext


object MissionRestHandlers {

    val onInfoRequest = Handler<RoutingContext> { routingContext ->
        MissionRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            MissionOperationIds.GET_MISSION,
            EMPTY_MESSAGE
        )
    }

    val onMedicUpdate = Handler<RoutingContext> { routingContext ->
        val medicName = routingContext.bodyAsJson
        MissionRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            MissionOperationIds.PUT_MEDIC,
            medicName
        )
    }

    val onMedicRequest = Handler<RoutingContext> { routingContext ->
        MissionRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            MissionOperationIds.GET_MEDIC,
            EMPTY_MESSAGE
        )
    }

    val onReturnInfoUpdate = Handler<RoutingContext> { routingContext ->
        MissionRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            MissionOperationIds.PUT_RETURN_INFO,
            routingContext.bodyAsJson
        )

    }

    val onReturnInfoRequest = Handler<RoutingContext> { routingContext ->
        MissionRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            MissionOperationIds.GET_RETURN_INFO,
            EMPTY_MESSAGE
        )
    }

    val onTrackingInfoRequest = Handler<RoutingContext> { routingContext ->
        MissionRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            MissionOperationIds.GET_TRACKING,
            EMPTY_MESSAGE
        )
    }

    val onDepartureFromHostpital = Handler<RoutingContext> { routingContext ->
        MissionRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            MissionOperationIds.DEPARTURE_FROM_HOSPITAL,
            EMPTY_MESSAGE
        )
    }

    val onArrivalOnSite = Handler<RoutingContext> { routingContext ->
        MissionRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            MissionOperationIds.ARRIVAL_ON_SITE,
            EMPTY_MESSAGE
        )
    }

    val onDepartureFromSite = Handler<RoutingContext> { routingContext ->
        MissionRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            MissionOperationIds.DEPARTURE_FROM_SITE,
            EMPTY_MESSAGE
        )
    }

    val onArrivalAtHostpital = Handler<RoutingContext> { routingContext ->
        MissionRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            MissionOperationIds.ARRIVAL_AT_THE_HOSPITAL,
            EMPTY_MESSAGE
        )
    }
}

object MissionOperationIds {
    const val GET_MISSION = "getMission"

    const val PUT_MEDIC = "setMissionMedic"
    const val GET_MEDIC = "getMissionMedic"

    const val PUT_RETURN_INFO = "setMissionReturnInfo"
    const val GET_RETURN_INFO = "getMissionReturnInfo"

    const val GET_TRACKING = "getMissionTrackingInfo"
    const val DEPARTURE_FROM_HOSPITAL = "departureFromHospital"
    const val ARRIVAL_ON_SITE = "arrivalOnSite"
    const val DEPARTURE_FROM_SITE = "departureFromSite"
    const val ARRIVAL_AT_THE_HOSPITAL = "arrivalAtTheHospital"
}
