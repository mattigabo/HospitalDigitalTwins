package hospitaldigitaltwins.traumamanagement.location

import digitaltwinframework.coreimplementation.restmanagement.EventBusRestRequestForwarder
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages
import io.vertx.core.Handler
import io.vertx.core.json.JsonArray
import io.vertx.ext.web.RoutingContext

/**
 * Created by Matteo Gabellini on 05/03/2020.
 */
object LocationRestHandlers {
    val onGetLocations = Handler<RoutingContext> { routingContext ->
        EventBusRestRequestForwarder.performRequest<JsonArray>(
            routingContext,
            LocationOperationIds.GET_LOCATIONS,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onEntryInLocation = Handler<RoutingContext> { routingContext ->
        EventBusRestRequestForwarder.performRequest<JsonArray>(
            routingContext,
            LocationOperationIds.ENTRY_IN_LOCATION,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onExitFromLocation = Handler<RoutingContext> { routingContext ->
        EventBusRestRequestForwarder.performRequest<JsonArray>(
            routingContext,
            LocationOperationIds.EXIT_FROM_LOCATION,
            StandardMessages.EMPTY_MESSAGE
        )
    }
}

object LocationOperationIds {
    const val GET_LOCATIONS = "getLocations"
    const val ENTRY_IN_LOCATION = "entryInLocation"
    const val EXIT_FROM_LOCATION = "exitFromLocation"
}