package hospitaldigitaltwins.traumamanagement

import digitaltwinframework.coreimplementation.AbstractDigitalTwin
import io.vertx.core.AbstractVerticle

/**
 * Created by Matteo Gabellini on 04/03/2020.
 */
class TraumaEvolutionController(val thisDT: AbstractDigitalTwin) : AbstractVerticle() {
    //val locationService = LocationService()
    //val traumaLeaderService = TraumaInfoService()
    //val releaseSiteService = ReleaseSiteService()
    //val patientService = PatientService()
/*    val eventService = EventService()

    var restRoutingAdapter = RESTRoutingAdapter(thisDT.runningEnv.vertx, thisDT.identifier.toString())

    override fun start() {
        super.start()
        registerCoreHandlerToEventBus(thisDT.runningEnv.eventBus)
        restRoutingAdapter.loadOpenApiSpec()
    }

    private fun registerCoreHandlerToEventBus(eb: EventBus) {
        eventService.registerEventBusConsumers(eb)
    }*/
}