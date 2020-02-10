package hospitaldigitaltwins.prehmanagement

import digitaltwinframework.coreimplementation.AbstractDigitalTwin
import hospitaldigitaltwins.prehmanagement.eventmanagement.EventService
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus


class PreHEvolutionController(val thisDT: AbstractDigitalTwin) : AbstractVerticle() {

    val eventService = EventService()

    var restRoutingAdapter = RESTRoutingAdapter(thisDT.runningEnv.vertx, thisDT.identifier.toString())

    override fun start() {
        super.start()
        registerCoreHandlerToEventBus(thisDT.runningEnv.eventBus)
        restRoutingAdapter.loadOpenApiSpec()
    }

    private fun registerCoreHandlerToEventBus(eb: EventBus) {
        eventService.registerEventBusConsumers(eb)
    }
}