package digitaltwinframework.coreimplementation.restmanagement

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import io.vertx.ext.web.RoutingContext

/**
 * Created by Matteo Gabellini on 10/02/2020.
 */
object RestResponse {
    fun <T> perform(routingContext: RoutingContext): Handler<AsyncResult<Message<T>>> {
        val responseHandler = Handler<AsyncResult<Message<T>>> { ar ->
            when {
                ar.succeeded() -> RESTDefaultResponse.sendSuccessResponse(
                    ar.result().body().toString(),
                    routingContext
                )
                ar.failed() -> RESTDefaultResponse.sendServerErrorResponse(
                    routingContext,
                    ar.cause().message!!.toString()
                )
                else -> RESTDefaultResponse.sendServerErrorResponse(routingContext)
            }
        }
        return responseHandler
    }
}