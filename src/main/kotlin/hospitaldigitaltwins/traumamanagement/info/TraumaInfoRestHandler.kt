package hospitaldigitaltwins.traumamanagement.info

import digitaltwinframework.coreimplementation.restmanagement.EventBusRestRequestForwarder
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext

/**
 * Created by Matteo Gabellini on 06/03/2020.
 */
object TraumaInfoRestHandler {

    val onGetInfoService = Handler<RoutingContext> { routingContext ->
        EventBusRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            TraumaInfoOperationIds.GET_BASIC_INFO,
            StandardMessages.EMPTY_MESSAGE
        )
    }
}

object TraumaInfoOperationIds {
    const val GET_BASIC_INFO = "getBasicInfo"
}