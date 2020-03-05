package hospitaldigitaltwins.prehmanagement.missions

import digitaltwinframework.coreimplementation.restmanagement.EventBusRestRequestForwarder
import digitaltwinframework.coreimplementation.restmanagement.RESTDefaultResponse
import io.vertx.ext.web.RoutingContext

/**
 * Created by Matteo Gabellini on 05/03/2020.
 */
object MissionRestRequestForwarder {
    fun <T> performRequest(routingContext: RoutingContext, busAdrr: String, message: Any) {
        routingContext.pathParams().get("missionId")?.let {
            EventBusRestRequestForwarder.performRequest<T>(routingContext, busAdrr + it, message)
        } ?: RESTDefaultResponse.sendBadRequestResponse("MissionId not specified", routingContext)
    }
}