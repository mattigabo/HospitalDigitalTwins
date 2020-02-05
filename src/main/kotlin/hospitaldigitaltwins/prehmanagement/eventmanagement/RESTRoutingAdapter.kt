package hospitaldigitaltwins.prehmanagement.eventmanagement

import digitaltwinframework.coreimplementation.restmanagement.AbstractRESTInteractionAdapter
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.ext.web.RoutingContext

class RESTRoutingAdapter(
        vertxInstance: Vertx,
        handlerServiceId: String
) : AbstractRESTInteractionAdapter(vertxInstance, handlerServiceId) {

    val onAddEventInfo = Handler<RoutingContext> { routingContext ->

    }

    val getEventInfo = Handler<RoutingContext> { routingContext ->

    }

    override fun getOpenApiSpec(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun operationCallbackMapping(): Map<String, Handler<RoutingContext>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

object EventOperationIDS {
    val POST_EVENT_INFO = "addEventInfo"
    val GET_EVENT_INFO = "getEventInfo"
}