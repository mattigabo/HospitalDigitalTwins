package hospitaldigitaltwins.traumamanagement.patient

import digitaltwinframework.EvolutionController
import digitaltwinframework.coreimplementation.BasicDigitalTwin
import digitaltwinframework.coreimplementation.BasicDigitalTwinSystem
import digitaltwinframework.coreimplementation.BasicEvolutionController
import digitaltwinframework.coreimplementation.VertxDigitalTwinExecutor
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
import java.net.URI

class PatientDT(dtIdentifier: URI, executor: VertxDigitalTwinExecutor) : BasicDigitalTwin(dtIdentifier, executor)


class PatientEvolutionController : EvolutionController, AbstractVerticle() {
    private val temperatureUpdatePeriod: Long = 1000

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