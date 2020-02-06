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


    }

    val onMedicUpdate = Handler<RoutingContext> { routingContext ->


    }

    val onMedicRequest = Handler<RoutingContext> { routingContext ->


    }

    val onReturnInfoUpdate = Handler<RoutingContext> { routingContext ->


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
