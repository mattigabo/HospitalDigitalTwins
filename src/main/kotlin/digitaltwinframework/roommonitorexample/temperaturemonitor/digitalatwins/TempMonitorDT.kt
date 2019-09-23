package digitaltwinframework.roommonitorexample.temperaturemonitor.digitalatwins

import digitaltwinframework.DigitalTwinMetaInfo
import digitaltwinframework.DigitalTwinValue
import digitaltwinframework.EvolutionController
import digitaltwinframework.PhysicalCounterpartModelData
import digitaltwinframework.coreimplementation.BasicDigitalTwin
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import java.net.URI
import java.time.Instant

class TempMonitorDT(identifier: URI, roomPosition: String) : BasicDigitalTwin(identifier) {
    override var modelData: TempMonitorModelData = TempMonitorModelData(roomPosition)
    override var metaInfo: TempMonitorDTMetaInfo = TempMonitorDTMetaInfo()
    override val evolutionManager = TempMonitorEvolutionController(modelData)

    init {
        Vertx.vertx().deployVerticle(evolutionManager)
    }

    override fun stop() {
        Vertx.vertx().undeploy(evolutionManager.deploymentID(), { res ->
            if (res.succeeded()) {
                println("Undeployed ok")
            } else {
                println("Undeploy failed!")
            }
        })
    }
}

class TempMonitorDTMetaInfo(val manufacturer: String = "FrameworkExample") : DigitalTwinMetaInfo

class TempMonitorModelData(roomPosition: String) : PhysicalCounterpartModelData {
    var currentTemperature: DigitalTwinValue<Int>? = null
    var measureUnit: String = "Celsius"
    var roomPosition: DigitalTwinValue<String>? = DigitalTwinValue(roomPosition, Instant.now())
}

class TempMonitorEvolutionController(var modelData: TempMonitorModelData) : EvolutionController, AbstractVerticle() {
    override fun start() {
        super.start()
        println("Evolution Manager Loading...")

        val eb = Vertx.vertx().eventBus()

        eb.consumer<Any>("EvolutionController", { message -> println(message.body()) })
    }

    override fun stop() {
        super.stop()
        println("Evolution Manager Termination")
    }
}



