package hospitaldigitaltwins.traumamanagement

import digitaltwinframework.coreimplementation.AbstractDigitalTwin
import hospitaldigitaltwins.traumamanagement.info.TraumaInfoService
import hospitaldigitaltwins.traumamanagement.patient.PatientService
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus

/**
 * Created by Matteo Gabellini on 04/03/2020.
 */
class TraumaEvolutionController(val thisDT: AbstractDigitalTwin) : AbstractVerticle() {
    val mongoConfigPath = "res/mongo/configTrauma.json"
    //val locationService = LocationService()
    val traumaInfoPromise = TraumaInfoService.createTrauma(mongoConfigPath)
    val patientServicePromise = PatientService.createPatient(mongoConfigPath)

    var restRoutingAdapter = TraumaRestRoutingAdapter(thisDT.runningEnv.vertx, thisDT.identifier.toString())

    override fun start() {
        super.start()
        registerCoreHandlerToEventBus(thisDT.runningEnv.eventBus)
        restRoutingAdapter.loadOpenApiSpec()
    }

    private fun registerCoreHandlerToEventBus(eb: EventBus) {
        traumaInfoPromise.future().onComplete {
            it.result().registerEventBusConsumers(eb)
        }
        patientServicePromise.future().onComplete {
            it.result().registerPatientWaitingConsumer(eb)
        }
    }
}