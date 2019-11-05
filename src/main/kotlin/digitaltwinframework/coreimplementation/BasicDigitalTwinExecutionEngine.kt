package digitaltwinframework.coreimplementation

import digitaltwinframework.DigitalTwinExecutionEngine
import digitaltwinframework.DigitalTwinFactory
import io.vertx.core.Vertx
import java.net.URI

/**
 * This class represents a basic implmentation of a executor for a digital twin that uses Vert.x environment
 * */
class BasicDigitalTwinExecutionEngine : DigitalTwinExecutionEngine {

    val vertx = Vertx.vertx()
    val eventBus = vertx.eventBus()

    companion object {
        var runningInstance: digitaltwinframework.coreimplementation.BasicDigitalTwinExecutionEngine? = null
            private set

        @JvmStatic
        fun boot(): digitaltwinframework.coreimplementation.BasicDigitalTwinExecutionEngine {
            runningInstance?.let {
                return runningInstance as digitaltwinframework.coreimplementation.BasicDigitalTwinExecutionEngine
            }

            return BasicDigitalTwinExecutionEngine()
        }
    }

    init {
        DigitalTwinExecutionEngine.runningInstance = this
    }

    override fun executeDigitalTwin(id: URI, factory: DigitalTwinFactory): URI {
        var dt = factory.create(id)
        this.runningDT.put(dt.identifier, dt)
        return dt.identifier
    }
}