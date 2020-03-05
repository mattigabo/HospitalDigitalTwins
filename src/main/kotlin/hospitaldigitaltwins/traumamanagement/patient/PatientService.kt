package hospitaldigitaltwins.traumamanagement.patient

import hospitaldigitaltwins.common.AbstractPatientService
import io.vertx.core.Promise


class PatientService private constructor(
    creationPromise: Promise<PatientService>
) : AbstractPatientService("res/mongo/configTrauma.json") {
    override var collection: String = "traumaPatient"
    override var busAddrSuffix: String = ""

    init {
        super.basicPatientInitFuture.onComplete {
            creationPromise.complete(this)
        }.onFailure {
            creationPromise.fail(it)
        }
    }

    companion object {
        fun createPatient(): Promise<PatientService> {
            val patientPromise = Promise.promise<PatientService>()
            PatientService(patientPromise)
            return patientPromise
        }
    }
}