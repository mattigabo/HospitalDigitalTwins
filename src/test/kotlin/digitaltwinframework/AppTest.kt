package digitaltwinframework

import digitaltwinframework.coreimplementation.BasicDigitalTwinSystem
import digitaltwinframework.coreimplementation.DTSystemEventBusAddresses
import digitaltwinframework.coreimplementation.RESTServer
import digitaltwinframework.roommonitorexample.temperaturemonitor.digitalatwins.TempMonitorDTFactory
import digitaltwinframework.roommonitorexample.temperaturemonitor.mockedphysicalsensors.MockedTempSensorWithRESTInterface
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import java.net.URI

class AppTest {

    fun testRESTServerRouteAdding() {
        val dtSystem = BasicDigitalTwinSystem.boot()

        Thread.sleep(5000)
        println("Add a route")
        var mockDtID = URI("dtProva")

        var dtRouter = Router.router(dtSystem.vertx)

        dtRouter.route().path("/prova2").handler { routingContext ->
            // This handler gets called for each request that arrives on the server
            val response = routingContext.response()
            response.putHeader("content-type", "text/plain")

            // Write to the response and end it
            response.end("Hello World!2")
        }

        dtSystem.eventBus.send(DTSystemEventBusAddresses.RESTServer.address, RESTServer.NewRouter(mockDtID, dtRouter))

        Thread.sleep(5000)
        println("Remove previous added route")
        dtSystem.eventBus.send(DTSystemEventBusAddresses.RESTServer.address, RESTServer.UnregisterRouter(mockDtID))


        Thread.sleep(10000)
        println("Shutdown all")
        dtSystem.shutdown()
    }

    fun testMockedSensorStart() {
        Vertx.vertx().deployVerticle(MockedTempSensorWithRESTInterface())
    }

    fun testDigitalTwinAndMockedSensorInteraction() {
        val dtSystem = BasicDigitalTwinSystem.boot()

        val mockedTempSensor = MockedTempSensorWithRESTInterface()
        dtSystem.vertx.deployVerticle(mockedTempSensor)
        dtSystem.createDigitalTwin(TempMonitorDTFactory("North"))

        Thread.sleep(10000)
        println("Shutdown all")
        dtSystem.shutdown()
    }
}

fun main() {
    println("Provo ad avviare il digital twin manager")
    AppTest().testDigitalTwinAndMockedSensorInteraction()

}