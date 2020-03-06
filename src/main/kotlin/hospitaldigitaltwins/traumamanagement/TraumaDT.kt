package hospitaldigitaltwins.traumamanagement

import digitaltwinframework.DigitalTwin
import digitaltwinframework.DigitalTwinFactory
import digitaltwinframework.coreimplementation.AbstractDigitalTwin

class TraumaDT(override val identifier: String) : AbstractDigitalTwin(identifier) {

    val traumaEvoController = TraumaEvolutionController(this)

    init {
        runningEnv.vertx.deployVerticle(traumaEvoController) { res ->
            if (res.succeeded()) {
                System.out.println("TraumaEvolutionController Deployment id is: " + res.result())
            } else {
                System.out.println("TraumaEvolutionController Deployment failed!")
            }
        }
    }

    override fun shutdown() {
        runningEnv.vertx.undeploy(traumaEvoController.deploymentID())
        super.shutdown()
    }

}

class TraumaDTFactory : DigitalTwinFactory {
    override fun create(id: String): DigitalTwin = TraumaDT(id)
}