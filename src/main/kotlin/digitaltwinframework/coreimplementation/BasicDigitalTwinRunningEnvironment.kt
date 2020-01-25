package digitaltwinframework.coreimplementation

import digitaltwinframework.DigitalTwin
import digitaltwinframework.DigitalTwinFactory
import digitaltwinframework.DigitalTwinRunningEnvironment
import digitaltwinframework.coreimplementation.restmanagement.DevelopmentConfigurations
import digitaltwinframework.coreimplementation.restmanagement.RESTServer
import io.vertx.core.Vertx
import java.net.URI

/**
 * This class represents a basic implementation of a executor for a digital twin that uses Vert.x environment
 * */
class BasicDigitalTwinRunningEnvironment(val environmentName: String) : DigitalTwinRunningEnvironment {

    val vertx = Vertx.vertx()
    val eventBus = vertx.eventBus()
    var restServer: RESTServer
    private var encapsulatedDT: DigitalTwin? = null

    companion object {
        var runningInstance: digitaltwinframework.coreimplementation.BasicDigitalTwinRunningEnvironment? = null
            private set

        @JvmStatic
        fun boot(environmentName: String): digitaltwinframework.coreimplementation.BasicDigitalTwinRunningEnvironment {
            runningInstance?.let {
                return it
            }

            return BasicDigitalTwinRunningEnvironment(environmentName)
        }
    }

    init {
        restServer = RESTServer(DevelopmentConfigurations.basicConfig, environmentName)
        vertx.deployVerticle(restServer)
    }

    override fun executeDigitalTwin(id: URI, factory: DigitalTwinFactory) {
        encapsulatedDT = factory.create(id)
    }

    override fun shutdown() {
        vertx.undeploy(restServer.deploymentID())
        System.exit(1)
    }
}