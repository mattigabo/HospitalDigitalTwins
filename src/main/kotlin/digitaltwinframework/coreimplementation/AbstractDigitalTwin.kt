package digitaltwinframework.coreimplementation

import digitaltwinframework.DigitalTwin

/**
 * This class constitutes an abstract implementation of a Digital Twin
 * that provide basic implementation of the management of the core aspects
 *
 * */
abstract class AbstractDigitalTwin(override val identifier: String) : DigitalTwin {

    val runningEnv = BasicRunningEnvironment.runningInstance!!

    val coreManagEvoController: CoreManagementEvolutionController = CoreManagementEvolutionController(this)

    init {
        runningEnv.vertx.deployVerticle(coreManagEvoController)
    }

    override fun shutdown() {
        runningEnv.vertx.undeploy(coreManagEvoController.deploymentID())
        runningEnv.shutdown()
    }
}

