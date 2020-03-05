package hospitaldigitaltwins.prehmanagement.patients

import hospitaldigitaltwins.common.AbstractPatientService
import io.vertx.core.Promise

class PatientService private constructor(
    missionId: Int,
    creationPromise: Promise<PatientService>
) : AbstractPatientService("res/mongo/configPreH.json") {

    override var patientCollection = "prehPatients"
    override var busAddrSuffix: String = missionId.toString()

    init {
        super.basicPatientInitFuture.onComplete {
            creationPromise.complete(this)
        }.onFailure {
            creationPromise.fail(it)
        }
    }


    companion object {
        fun createPatient(missionId: Int): Promise<PatientService> {
            val patientPromise = Promise.promise<PatientService>()
            PatientService(missionId, patientPromise)
            return patientPromise
        }
    }
}