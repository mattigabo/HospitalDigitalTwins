package digitaltwinframework.coreimplementation

import digitaltwinframework.DigitalTwinExecutor
import digitaltwinframework.DigitalTwinFactory
import io.vertx.core.Vertx
import java.net.URI

/**
 * This class represents a basic implmentation of a executor for a digital twin that uses Vert.x environment
 * */
class VertxDigitalTwinExecutor : DigitalTwinExecutor {
    val vertx = Vertx.vertx()
    val eventBus = vertx.eventBus()

    companion object {
        var runningInstance: VertxDigitalTwinExecutor? = null
            private set

        @JvmStatic
        fun boot(): VertxDigitalTwinExecutor {
            runningInstance?.let {
                return runningInstance as VertxDigitalTwinExecutor
            }

            return VertxDigitalTwinExecutor()
        }
    }

    init {
        VertxDigitalTwinExecutor.runningInstance = this
    }

    override fun executeDigitalTwin(id: URI, factory: DigitalTwinFactory): URI {
        var dt = factory.create(id)
        this.runningDT.put(dt.identifier, dt)
        return dt.identifier
    }
}