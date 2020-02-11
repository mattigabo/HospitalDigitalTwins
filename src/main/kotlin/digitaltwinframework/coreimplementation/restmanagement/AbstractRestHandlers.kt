package digitaltwinframework.coreimplementation.restmanagement

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.ext.web.RoutingContext

/**
 * Created by Matteo Gabellini on 10/02/2020.
 */
abstract class AbstractRestHandlers {
    val eb = Vertx.currentContext().owner().eventBus()

    protected fun <T> responseCallBack(routingContext: RoutingContext): Handler<AsyncResult<Message<T>>> {
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