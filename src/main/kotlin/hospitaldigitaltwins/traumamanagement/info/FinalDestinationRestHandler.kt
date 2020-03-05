package hospitaldigitaltwins.traumamanagement.info

import digitaltwinframework.coreimplementation.restmanagement.EventBusRestRequestForwarder
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext

/**
 * Created by Matteo Gabellini on 05/03/2020.
 */
object FinalDestinationRestHandler {
    val onSetFinalDestination = Handler<RoutingContext> { routingContext ->
        val finalDestination = routingContext.bodyAsJson
        EventBusRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            FinalDestinationOperationIds.SET_FINAL_DESTINATION,
            finalDestination
        )
    }

    val onGetFinalDestination = Handler<RoutingContext> { routingContext ->
        EventBusRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            FinalDestinationOperationIds.GET_FINAL_DESTINATION,
            StandardMessages.EMPTY_MESSAGE
        )
    }
}

object FinalDestinationOperationIds {
    const val SET_FINAL_DESTINATION = "setFinalDestination"
    const val GET_FINAL_DESTINATION = "getFinalDestination"
}