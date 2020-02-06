package digitaltwinframework.coreimplementation

import digitaltwinframework.DigitalTwin
import java.net.URI

/**
 * This class constitutes an abstract implementation of a Digital Twin
 * that provide basic implementation of the management of the core aspects
 *
 * */
abstract class AbstractDigitalTwin(override val identifier: URI) : DigitalTwin {

    private var shutdownStarted = false
    val runningEnv = BasicDigitalTwinRunningEnvironment.runningInstance!!

    val evolutionController: CoreManagementEvolutionController = CoreManagementEvolutionController(this)

    init {
        runningEnv.vertx.deployVerticle(evolutionController)
    }

    override fun shutdown() {
        runningEnv.vertx.undeploy(evolutionController.deploymentID())
        runningEnv.shutdown()
    }
}

