package hospitaldigitaltwins.prehmanagement

import digitaltwinframework.DigitalTwin
import digitaltwinframework.DigitalTwinFactory
import digitaltwinframework.coreimplementation.AbstractDigitalTwin
import java.net.URI

class PreHDT(override val identifier: URI) : AbstractDigitalTwin(identifier) {

    val eventManagEvoController = PreHEvolutionController(this)

    init {
        runningEnv.vertx.deployVerticle(eventManagEvoController) { res ->
            if (res.succeeded()) {
                System.out.println("PreHEvolutionController Deployment id is: " + res.result())
            } else {
                System.out.println("PreHEvolutionController Deployment failed!")
            }
        }
    }

    override fun shutdown() {
        runningEnv.vertx.undeploy(eventManagEvoController.deploymentID())
        super.shutdown()
    }
}


class PreHDTFactory : DigitalTwinFactory {
    override fun create(id: URI): DigitalTwin = PreHDT(id)
}