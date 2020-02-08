package digitaltwinframework

//import digitaltwinframework.coreimplementation.BasicDigitalTwinSystem

class AppTest {

    /*fun testRESTServerRouteAdding() {
        val dtSystem = BasicDigitalTwinSystem.boot()
        dtSystem.eventBus.registerDefaultCodec(Temperature::class.java, TemperatureMessageCodec())

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

        dtSystem.eventBus.send(SystemEventBusAddresses.RESTServer.address, RESTServer.DTRouter(mockDtID, dtRouter))

        Thread.sleep(10000)
        println("Remove previous added route")
        dtSystem.eventBus.send(SystemEventBusAddresses.RESTServer.address, RESTServer.UnregisterRouter(mockDtID, dtRouter))


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

        Thread.sleep(5000)

        //val dtId = dtSystem.createDigitalTwin(TempMonitorDTFactory("North"))
        //println("Digital Twin created with ID ${dtId}")

        Thread.sleep(20000)
        println("Shutdown all")
        dtSystem.shutdown()
    }
*/

    fun testBasicDigitalTwin() {
        println("Test a basic digital twin boot")
        //BasicRunningEnvironment("testEnvironment")
    }


}

fun main() {

    //Test Digital Twin Creation
    AppTest().testBasicDigitalTwin()

    //AppTest().testRESTServerRouteAdding()

}