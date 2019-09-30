package digitaltwinframework.examples.roommonitor.temperaturemonitor.digitalatwins

import digitaltwinframework.PhysicalWorldInteractionAdapter
import digitaltwinframework.coreimplementation.BasicDigitalTwinSystem
import digitaltwinframework.roommonitorexample.temperaturemonitor.digitalatwins.TempMonitorDT
import digitaltwinframework.roommonitorexample.temperaturemonitor.mockedphysicalsensors.MockedTempSensorWithRESTInterface
import io.vertx.core.Vertx
import io.vertx.ext.web.client.WebClient
import java.time.Instant

class PhysicalTempSensorAdapter(val thisDT: TempMonitorDT) : PhysicalWorldInteractionAdapter {

    private var physicalDeviceHost = MockedTempSensorWithRESTInterface.host
    private var portNumber = 8081

    var client = WebClient.create(Vertx.vertx())

    fun requestTemperature() {
        client.get(portNumber, this.physicalDeviceHost, "/temperature").send { ar ->
            if (ar.succeeded()) {
                // Obtain response
                var response = ar.result()

                val jObjResponse = response.bodyAsJsonObject()
                val value: Int = jObjResponse.getInteger("value")
                val unit: String = jObjResponse.getString("unit")
                val timestamp: Instant = jObjResponse.getInstant("timestamp")
                val temp = Temperature(value, unit, timestamp)

                BasicDigitalTwinSystem.RUNNING_INSTANCE!!.eventBus.send(thisDT.EVOLUTION_CONTROLLER_ADDRESS, temp)
            } else {
                println("Something went wrong ${ar.cause().message}")
            }
        }
    }


}