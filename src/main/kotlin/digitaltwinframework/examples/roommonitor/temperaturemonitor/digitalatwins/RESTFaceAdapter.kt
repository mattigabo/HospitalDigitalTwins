package digitaltwinframework.examples.roommonitor.temperaturemonitor.digitalatwins

import digitaltwinframework.InteractionAdapter
import digitaltwinframework.coreimplementation.BasicDigitalTwinSystem
import digitaltwinframework.coreimplementation.RESTServer
import digitaltwinframework.coreimplementation.SystemEventBusAddresses
import digitaltwinframework.roommonitorexample.temperaturemonitor.digitalatwins.TempMonitorDT
import io.vertx.core.Handler
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory


class RESTFaceAdapter(val thisDT: TempMonitorDT) : InteractionAdapter {

    private var temperatureRequestHandler = Handler<RoutingContext> { routingContext: RoutingContext ->
        BasicDigitalTwinSystem.RUNNING_INSTANCE?.let {
            it.eventBus.request<String>(thisDT.EVOLUTION_CONTROLLER_ADDRESS, OperationIDS.TEMPERATURE_MEASURED) { ar ->
                if (ar.succeeded()) {
                    sendResponse(ar.result().body(), routingContext)
                }
            }
        }
    }

    private fun sendResponse(response: String, routingContext: RoutingContext) {
        routingContext
                .response()
                .setStatusCode(200)
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .end(response)
    }

    fun loadOpenApiSpec() {
        OpenAPI3RouterFactory.create(io.vertx.core.Vertx.vertx(), thisDT.metaInfo.openApiSpecificationPath) { asyncResult ->
            if (asyncResult.succeeded()) {
                var routerFactory: OpenAPI3RouterFactory = asyncResult.result()

                routerFactory.addHandlerByOperationId(OperationIDS.TEMPERATURE_MEASURED, temperatureRequestHandler)

                this.registerToTheRunningServer(routerFactory.router)
            } else {
                println("ERROR")
                // Something went wrong during router factory initialization
                val exception = asyncResult.cause() // (2)
                println(exception)
            }
        }
    }

    private fun registerToTheRunningServer(router: Router) {
        BasicDigitalTwinSystem.RUNNING_INSTANCE?.let {
            it.eventBus.send(SystemEventBusAddresses.RESTServer.address, RESTServer.NewRouter(thisDT.identifier, router))
        }
    }
}

object OperationIDS {
    const val TEMPERATURE_MEASURED = "temperatureMeasured"
}