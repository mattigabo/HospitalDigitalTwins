package hospitaldigitaltwins.prehmanagement.missions

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

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


object MissionRestHandlers {

    val onInfoRequest = Handler<RoutingContext> { routingContext ->
        println("onInfoRequest")
    }

    val onMedicUpdate = Handler<RoutingContext> { routingContext ->
        println("onMedicUpdate")
    }

    val onMedicRequest = Handler<RoutingContext> { routingContext ->
        println("onMedicRequest")
    }

    val onReturnInfoUpdate = Handler<RoutingContext> { routingContext ->
        println("onReturnInfoUpdate")
    }

    val onReturnInfoRequest = Handler<RoutingContext> { routingContext ->


    }

    val onTrackingInfoRequest = Handler<RoutingContext> { routingContext ->


    }

    val onDepartureFromHostpital = Handler<RoutingContext> { routingContext ->


    }

    val onArrivalOnSite = Handler<RoutingContext> { routingContext ->


    }

    val onDepartureFromSite = Handler<RoutingContext> { routingContext ->


    }

    val onArrivalAtHostpital = Handler<RoutingContext> { routingContext ->


    }
}
