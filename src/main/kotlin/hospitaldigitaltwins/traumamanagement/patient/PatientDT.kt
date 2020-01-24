package hospitaldigitaltwins.traumamanagement.patient

import digitaltwinframework.coreimplementation.AbstractDigitalTwin
import digitaltwinframework.coreimplementation.BasicDigitalTwinExecutionEngine
import digitaltwinframework.coreimplementation.BasicDigitalTwinSystem
import digitaltwinframework.coreimplementation.BasicEvolutionController
import io.vertx.core.eventbus.EventBus
import java.net.URI

class PatientDT(
    dtIdentifier: URI,
    executor: BasicDigitalTwinExecutionEngine
) : AbstractDigitalTwin(dtIdentifier, executor) {

    override val evolutionController: PatientEvolutionController = PatientEvolutionController(this)
}

class PatientEvolutionController(override val thisDT: PatientDT) : BasicEvolutionController(thisDT) {

    override fun start() {
        super.start()
        BasicDigitalTwinSystem.RUNNING_INSTANCE?.let {

            this.registerToEventBus(it.eventBus)
        }
    }

    override fun stop() {
        super.stop()
        println("Evolution Manager Termination")
    }

    private fun registerToEventBus(eb: EventBus) {
        eb.consumer<Any>(thisDT.EVOLUTION_CONTROLLER_ADDRESS) { message ->
            println(message)
        }
    }

    //
}