package digitaltwinframework.roommonitorexample.temperaturemonitor.mockedphysicalsensors

import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory
import java.time.Instant
import kotlin.random.Random

class SimulatedTempSensor {
    val measureUnit = "celsius"
    fun readValue(): Int {
        return Random.nextInt() % 30
    }

    fun jsonFormatReadValue(): String {
        return """{ 
                "value":${this.readValue()},
                "unit":"${this.measureUnit}",
                "timestamp":"${Instant.now()}"
            }"""
    }
}

class MockedTempSensorWithRESTInterface : AbstractVerticle() {
    companion object {
        var host = "localhost"
        //val host = "192.168.0.13"
        val portNumber = 8081
        val openApiSpecPath =
            "file://${System.getProperty("user.dir")}/res/framework/examples/TemperatureSensor-1.0.0-OpenAPI-Schema.yaml".replace(
                " ",
                "%20"
            )
    }

    private lateinit var server: HttpServer

    private var sensor = SimulatedTempSensor()

    private var temperatureRequestHandler = { routingContext: RoutingContext ->
        routingContext
                .response()
                .setStatusCode(200)
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json") // (2)
                .end(sensor.jsonFormatReadValue()) // (3)
    }

    override fun start() {
        super.start()
        initServer()

    }

    override fun stop() {
        super.stop()
        this.server.close()
    }

    fun initServer() {
        vertx.executeBlocking<Router>({ promise ->
            OpenAPI3RouterFactory.create(this.vertx, openApiSpecPath) { asyncResult ->
                if (asyncResult.succeeded()) {
                    var routerFactory: OpenAPI3RouterFactory = asyncResult.result() // (1)

                    routerFactory.addHandlerByOperationId("temperatureMeasured", temperatureRequestHandler)

                    promise.complete(routerFactory.router)
                } else {
                    println("MockedTemperatureStation with REST interface: Failed to create router")
                    // Something went wrong during router factory initialization
                    var exception = asyncResult.cause() // (2)
                    println(exception)
                }
            }
        }, { res ->
            this.initServer(res.result())
        })
    }

    private fun initServer(router: Router) {
        server = this.vertx.createHttpServer()

        server.requestHandler(router).listen(portNumber, host) { res ->
            if (res.succeeded()) {
                println("MockedTemperatureStation with REST interface is now listening!")
            } else {
                println("MockedTemperatureStation with REST interface: Failed to bind!")
                println(res.cause())
            }
        }
    }
}