package hospitaldigitaltwins.prehmanagement

import digitaltwinframework.DigitalTwin
import digitaltwinframework.DigitalTwinFactory
import digitaltwinframework.coreimplementation.AbstractDigitalTwin

class PreHDT(override val identifier: String) : AbstractDigitalTwin(identifier) {

    val preHEvoController = PreHEvolutionController(this)

    init {
        runningEnv.vertx.deployVerticle(preHEvoController) { res ->
            if (res.succeeded()) {
                System.out.println("PreHEvolutionController Deployment id is: " + res.result())
            } else {
                System.out.println("PreHEvolutionController Deployment failed!")
            }
        }
    }

    override fun shutdown() {
        runningEnv.vertx.undeploy(preHEvoController.deploymentID())
        super.shutdown()
    }
}


class PreHDTFactory : DigitalTwinFactory {
    override fun create(id: String): DigitalTwin = PreHDT(id)
}