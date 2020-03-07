package digitaltwinframework.coreimplementation

import digitaltwinframework.DigitalTwin
import digitaltwinframework.DigitalTwinFactory
import digitaltwinframework.DigitalTwinRunningEnvironment
import digitaltwinframework.coreimplementation.restmanagement.DevelopmentConfigurations
import digitaltwinframework.coreimplementation.restmanagement.RESTServerConfig
import digitaltwinframework.coreimplementation.restmanagement.RestServer
import io.vertx.core.Future
import io.vertx.core.Promise
import io.vertx.core.Vertx
import java.net.URI

/**
 * This class represents a basic implementation of a executor for a digital twin that uses Vert.x environment
 * */
class BasicRunningEnvironment private constructor(override val name: String) : DigitalTwinRunningEnvironment {

    val vertx = Vertx.vertx()
    val eventBus = vertx.eventBus()
    private var restServer: RestServer? = null
    private var encapsulatedDT: DigitalTwin? = null

    companion object {
        var runningInstance: BasicRunningEnvironment? = null
            private set

        @JvmStatic
        fun boot(
            environmentName: String,
            serverConfig: RESTServerConfig = DevelopmentConfigurations.basicConfig
        ): Future<DigitalTwinRunningEnvironment> {
            val bootPromise = Promise.promise<DigitalTwinRunningEnvironment>()
            runningInstance?.let {
                bootPromise.complete(it)
            }
            runningInstance = BasicRunningEnvironment(environmentName)
            runningInstance!!.startRestServer(serverConfig).onComplete {
                System.out.println(it)
                bootPromise.complete(runningInstance!!)
            }.onFailure { bootPromise.fail(it.cause) }
            return bootPromise.future()
        }
    }

    private fun startRestServer(config: RESTServerConfig): Future<String> {
        val serverBootPromise = Promise.promise<String>()
        restServer = RestServer(config, name)
        vertx.deployVerticle(restServer) { res ->
            if (res.succeeded()) {
                serverBootPromise.complete("REST Server Deployed")
            } else {
                serverBootPromise.fail("REST Server Deployment failed!")
            }
        }
        return serverBootPromise.future()
    }

    override fun executeDigitalTwin(id: URI, factory: DigitalTwinFactory) {
        encapsulatedDT = factory.create(id)
    }

    override fun shutdown() {
        restServer?.let { vertx.undeploy(it.deploymentID()) }
        System.exit(1)
    }
}