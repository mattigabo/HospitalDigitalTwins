package digitaltwinframework.examples.roommonitor.temperaturemonitor.digitalatwins

import digitaltwinframework.InteractionAdapter
import digitaltwinframework.coreimplementation.BasicDigitalTwinSystem
import digitaltwinframework.coreimplementation.DTSystemEventBusAddresses
import digitaltwinframework.coreimplementation.RESTServer
import digitaltwinframework.roommonitorexample.temperaturemonitor.mockedphysicalsensors.SimulatedTempSensor
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory
import java.net.URI


class RESTFaceAdapter(private val openApiSpecPath: String, val digitalTwinId: URI) : InteractionAdapter {

    var sensor = SimulatedTempSensor()

    private var temperatureRequestHandler = { routingContext: RoutingContext ->
        routingContext
                .response()
                .setStatusCode(200)
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json") // (2)
                .end(sensor.jsonFormatReadValue()) // (3)
    }

    fun loadOpenApiSpec() {
        OpenAPI3RouterFactory.create(io.vertx.core.Vertx.vertx(), openApiSpecPath) { asyncResult ->
            if (asyncResult.succeeded()) {
                var routerFactory: OpenAPI3RouterFactory = asyncResult.result()

                routerFactory.addHandlerByOperationId("temperatureMeasured", temperatureRequestHandler)

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
            it.eventBus.send(DTSystemEventBusAddresses.RESTServer.address, RESTServer.NewRouter(digitalTwinId, router))
        }
    }
}