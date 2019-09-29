package digitaltwinframework.roommonitorexample.temperaturemonitor.digitalatwins

import digitaltwinframework.*
import digitaltwinframework.coreimplementation.BasicDigitalTwin
import digitaltwinframework.coreimplementation.BasicDigitalTwinSystem
import digitaltwinframework.examples.roommonitor.temperaturemonitor.digitalatwins.PhysicalTempSensorAdapter
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import java.net.URI
import java.time.Instant

class TempMonitorDT(identifier: URI, roomPosition: String) : BasicDigitalTwin(identifier) {
    override var modelData: TempMonitorModelData = TempMonitorModelData(roomPosition)
    override var metaInfo: TempMonitorDTMetaInfo = TempMonitorDTMetaInfo()

    val physicalCounterPartAdapter = PhysicalTempSensorAdapter()
    override val evolutionManager = TempMonitorEvolutionController(modelData, physicalCounterPartAdapter)
    //fun dtRestFaceAdapter = RESTFaceAdapter(metaInfo.openApiSpecificationPath)

    init {
        Vertx.vertx().deployVerticle(evolutionManager)
    }

    override fun stop() {
        Vertx.vertx().undeploy(evolutionManager.deploymentID()) { res ->
            if (res.succeeded()) {
                println("Undeployed ok")
            } else {
                println("Undeploy failed!")
            }
        }
    }
}

class TempMonitorDTMetaInfo(val manufacturer: String = "FrameworkExample") : DigitalTwinMetaInfo {
    val openApiSpecificationPath = "file://${System.getProperty("user.dir")}/res/framework/examples/TemperatureMonitorDigitalTwin-1.0.0-OpenApi-schema.yaml"
}

class TempMonitorModelData(roomPosition: String) : PhysicalCounterpartModelData {
    var currentTemperature: DigitalTwinValue<Int>? = null
    var measureUnit: String = "Celsius"
    var roomPosition: DigitalTwinValue<String>? = DigitalTwinValue(roomPosition, Instant.now())
}


data class Temperature(override val value: Int, val unit: String, val measureTimestamp: Instant) : DigitalTwinValue<Int>(value, measureTimestamp)

class TempMonitorEvolutionController(var modelData: TempMonitorModelData, var physicalCounterpart: PhysicalTempSensorAdapter) : EvolutionController, AbstractVerticle() {
    private val temperatureUpdatePeriod: Long = 1000

    override fun start() {
        super.start()

        val eb = Vertx.vertx().eventBus()

        eb.consumer<Any>("EvolutionController", { message -> println(message.body()) })

        BasicDigitalTwinSystem.RUNNING_INSTANCE?.let {
            it.vertx.setPeriodic(temperatureUpdatePeriod) { id ->
                physicalCounterpart.requestTemperature()
            }
        }
    }

    override fun stop() {
        super.stop()
        println("Evolution Manager Termination")
    }


}


class TempMonitorDTFactory(var position: String) : DigitalTwinFactory {
    override fun create(id: URI): DigitalTwin = TempMonitorDT(id, position)
}
