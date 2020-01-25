package digitaltwinframework.examples.roommonitor.temperaturemonitor.digitalatwins
/*
import digitaltwinframework.coreimplementation.restmanagement.AbstractRESTInteractionAdapter
import digitaltwinframework.coreimplementation.BasicDigitalTwinSystem
import digitaltwinframework.coreimplementation.restmanagement.RESTServer
import digitaltwinframework.coreimplementation.utils.eventbusutils.SystemEventBusAddresses
import digitaltwinframework.roommonitorexample.temperaturemonitor.digitalatwins.TempMonitorDT
import io.vertx.core.Handler
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory


class TempMonitorRESTInteractionAdapter(override val thisDT: TempMonitorDT) : AbstractRESTInteractionAdapter(thisDT) {

    private var temperatureRequestHandler = Handler<RoutingContext> { routingContext: RoutingContext ->
        BasicDigitalTwinSystem.RUNNING_INSTANCE?.let {
            it.eventBus.request<String>(thisDT.EVOLUTION_CONTROLLER_ADDRESS, OperationIDS.TEMPERATURE_MEASURED) { ar ->
                if (ar.succeeded()) {
                    sendResponse(ar.result().body(), routingContext)
                }
            }
        }
    }


    fun loadOpenApiSpec() {
        OpenAPI3RouterFactory.create(thisDT.dtSystem.vertx, thisDT.metaInfo.openApiSpecificationPath) { asyncResult ->
            if (asyncResult.succeeded()) {
                var routerFactory: OpenAPI3RouterFactory = asyncResult.result()

                routerFactory.addHandlerByOperationId(OperationIDS.TEMPERATURE_MEASURED, temperatureRequestHandler)

                this.registerToTheRunningServer(routerFactory.router)
            } else {
                println("ERROR")
                // Something went wrong during router factory initialization
                val exception = asyncResult.cause()
                println(exception)
            }
        }
    }

    private fun registerToTheRunningServer(router: Router) {
        thisDT.dtSystem
                .eventBus
                .send(SystemEventBusAddresses.RESTServer.address, RESTServer.DTRouter(thisDT.identifier, router))

    }
}

object OperationIDS {
    const val TEMPERATURE_MEASURED = "temperatureMeasured"
}*/