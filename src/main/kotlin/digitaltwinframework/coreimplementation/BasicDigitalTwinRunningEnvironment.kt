package digitaltwinframework.coreimplementation

import digitaltwinframework.DigitalTwinFactory
import digitaltwinframework.DigitalTwinRunningEnvironment
import io.vertx.core.Vertx
import java.net.URI

/**
 * This class represents a basic implementation of a executor for a digital twin that uses Vert.x environment
 * */
class BasicDigitalTwinRunningEnvironment : DigitalTwinRunningEnvironment {

    val vertx = Vertx.vertx()
    val eventBus = vertx.eventBus()
    var restServer: RESTServer


    companion object {
        var runningInstance: digitaltwinframework.coreimplementation.BasicDigitalTwinRunningEnvironment? = null
            private set

        @JvmStatic
        fun boot(): digitaltwinframework.coreimplementation.BasicDigitalTwinRunningEnvironment {
            runningInstance?.let {
                return runningInstance as digitaltwinframework.coreimplementation.BasicDigitalTwinRunningEnvironment
            }

            return BasicDigitalTwinRunningEnvironment()
        }
    }

    init {
        restServer = RESTServer(this)
        vertx.deployVerticle(restServer)
    }

    override fun executeDigitalTwin(id: URI, factory: DigitalTwinFactory): URI {
        var dt = factory.create(id)
        this.runningDT.put(dt.identifier, dt)
        return dt.identifier
    }
}