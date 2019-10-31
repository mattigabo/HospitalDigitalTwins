package digitaltwinframework.roommonitorexample.temperaturemonitor.digitalatwins

import digitaltwinframework.*
import digitaltwinframework.coreimplementation.BasicDigitalTwin
import digitaltwinframework.coreimplementation.BasicDigitalTwinSystem
import digitaltwinframework.coreimplementation.SystemEventBusAddresses
import digitaltwinframework.examples.roommonitor.temperaturemonitor.digitalatwins.*
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
import java.net.URI
import java.time.Instant

class TempMonitorDT(identifier: URI, roomPosition: String) : BasicDigitalTwin(identifier) {
    override var dataModel: TempMonitorDataModel = TempMonitorDataModel(roomPosition)
    override var metaInfo: TempMonitorDTMetaInfo = TempMonitorDTMetaInfo()

    override val evolutionManager = TempMonitorEvolutionController(this)

    val EVOLUTION_CONTROLLER_ADDRESS = SystemEventBusAddresses.EVOLUTION_CONTROLLER_SUFFIX.preappend(identifier.toString())

    val physicalCounterPartAdapter = PhysicalTempSensorAdapter(this)

    val restFace = RESTFaceAdapter(this)

    init {
        this.dtSystem.let {
            it.vertx.deployVerticle(evolutionManager)
            it.eventBus.registerDefaultCodec(Temperature::class.java, TemperatureMessageCodec())

            restFace.loadOpenApiSpec()
        }
    }

    override fun stop() {
        this.dtSystem.let {
            it.vertx.undeploy(evolutionManager.deploymentID())
            it.eventBus.unregisterCodec(TemperatureMessageCodec().name())
        }
    }
}

class TempMonitorDTMetaInfo(val manufacturer: String = "FrameworkExample") : DigitalTwinMetaInfo {
    val openApiSpecificationPath =
        "file://${System.getProperty("user.dir")}/res/framework/examples/TemperatureMonitorDigitalTwin-1.0.0-OpenApi-schema.yaml".replace(
            " ",
            "%20"
        )
}

class TempMonitorDataModel(roomPosition: String) : PhysicalAssetDataModel {
    var temperature: Temperature? = null
    var roomLocation: DigitalTwinValue<String>? = DigitalTwinValue(roomPosition, Instant.now())
}

class TempMonitorEvolutionController(var thisDT: TempMonitorDT) : EvolutionController, AbstractVerticle() {
    private val temperatureUpdatePeriod: Long = 1000

    override fun start() {
        super.start()
        BasicDigitalTwinSystem.RUNNING_INSTANCE?.let {

            this.registerToEventBus(it.eventBus)

            it.vertx.setPeriodic(temperatureUpdatePeriod) { id ->
                thisDT.physicalCounterPartAdapter.requestTemperature()
            }
        }
    }

    private fun registerToEventBus(eb: EventBus) {
        eb.consumer<Any>(thisDT.EVOLUTION_CONTROLLER_ADDRESS) { message ->
            when (message.body()) {
                is Temperature -> {
                    println("Temperature received from Physical CounterPart: ${message.body()}")

                    thisDT.dataModel.temperature = message.body() as Temperature
                }

                OperationIDS.TEMPERATURE_MEASURED -> message.reply("""
                   {
                        "value":{${thisDT.dataModel.temperature!!.value}},
                        "unit":"{${thisDT.dataModel.temperature!!.unit}}",
                        "timestamp":"{${thisDT.dataModel.temperature!!.generationTime}}",
                        "roomLocation":":{${thisDT.dataModel.roomLocation!!.value}}"
                   }
               """.trimIndent())
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
