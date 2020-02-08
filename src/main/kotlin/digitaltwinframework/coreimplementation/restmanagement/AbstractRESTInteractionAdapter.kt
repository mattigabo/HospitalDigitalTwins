package digitaltwinframework.coreimplementation.restmanagement

import digitaltwinframework.InteractionAdapter
import digitaltwinframework.coreimplementation.utils.eventbusutils.SystemEventBusAddresses
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory

/**
 * This class represents an abstract interaction adapter between the digital twin passed as parameter and the REST Server.
 * The handler defined in the classes that implement this class will be used by the REST server control flow in order to forward the received
 * request to the digital twin evolution controller
 * */

abstract class AbstractRESTInteractionAdapter(val vertxInstance: Vertx, val handlerServiceId: String) : InteractionAdapter {

    abstract val openApiSpecPath: String

    abstract fun operationCallbackMapping(): Map<String, Handler<RoutingContext>>


    fun sendSuccessResponse(response: String, routingContext: RoutingContext) {
        routingContext
                .response()
                .setStatusCode(200)
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .end(response)
    }

    fun sendBadRequestResponse(response: String, routingContext: RoutingContext) {
        routingContext.response().setStatusCode(400).end(response)
    }

    fun sendNotFoundResponse(response: String, routingContext: RoutingContext) {
        routingContext.response().setStatusCode(404).end(response)
    }

    fun sendServerErrorResponse(routingContext: RoutingContext) {
        routingContext.response().setStatusCode(500).end("Server Error")
    }

    fun loadOpenApiSpec() {
        OpenAPI3RouterFactory.create(vertxInstance, openApiSpecPath) { asyncResult ->
            if (asyncResult.succeeded()) {
                var routerFactory: OpenAPI3RouterFactory = asyncResult.result()

                operationCallbackMapping().forEach {
                    routerFactory.addHandlerByOperationId(it.key, it.value)
                }

                this.registerToTheRunningServer(routerFactory.router)
            } else {
                println("Error during the {adapterName} loading! Something went wrong during router factory initialization!")
                val exception = asyncResult.cause()
                println(exception)
            }
        }
    }

    private fun registerToTheRunningServer(router: Router) {
        vertxInstance.eventBus()
                .send(SystemEventBusAddresses.RESTServer.address,
                        RESTServer.RegisterSubrouter(handlerServiceId, router))

    }

}

object RESTResponseCode {
    const val OK = 200
    const val CREATED = 201
    const val NO_CONTENT = 204
    const val BAD_PARAMETERS = 400
    const val NOT_FOUND = 404
    const val CONFLICT = 409
    const val INTERNAL_SEVER_ERROR = 500
}