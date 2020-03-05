package digitaltwinframework.coreimplementation.restmanagement

import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext

/**
 * Created by Matteo Gabellini on 05/03/2020.
 */
object EventBusRestRequestForwarder {
    fun eventBusInstance() = Vertx.currentContext().owner().eventBus()
    fun <T> performRequest(routingContext: RoutingContext, busAdrr: String, message: Any) {
        eventBusInstance()
            .request<JsonObject>(busAdrr, message, RestResponse.perform(routingContext))
    }
}