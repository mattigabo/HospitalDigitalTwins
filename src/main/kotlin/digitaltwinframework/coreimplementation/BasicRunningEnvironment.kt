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
class BasicRunningEnvironment private constructor(override val name: String) : DigitalTwinRunningEnvironment {

    val vertx = Vertx.vertx()
    val eventBus = vertx.eventBus()
    private var restServer: RESTServer? = null
    private var encapsulatedDT: DigitalTwin? = null

    companion object {
        var runningInstance: BasicRunningEnvironment? = null
            private set

        @JvmStatic
        fun boot(environmentName: String, closure: (DigitalTwinRunningEnvironment) -> Unit) {
            runningInstance?.let {
                closure(it)
            }
            runningInstance = BasicRunningEnvironment(environmentName)
            runningInstance!!.startRestServer(closure)
        }
    }

    private fun startRestServer(closure: (DigitalTwinRunningEnvironment) -> Unit) {
        restServer = RESTServer(DevelopmentConfigurations.basicConfig, name)
        vertx.deployVerticle(restServer) { res ->
            if (res.succeeded()) {
                System.out.println("REST Server Deployed")
                closure(this)
            } else {
                System.out.println("REST Server Deployment failed!")
            }
        }
    }

    override fun executeDigitalTwin(id: URI, factory: DigitalTwinFactory) {
        encapsulatedDT = factory.create(id)
    }

    override fun shutdown() {
        restServer?.let { vertx.undeploy(it.deploymentID()) }
        System.exit(1)
    }
}