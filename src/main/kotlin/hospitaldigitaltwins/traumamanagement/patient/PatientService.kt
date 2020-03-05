package hospitaldigitaltwins.traumamanagement.patient

import hospitaldigitaltwins.common.AbstractPatientService
import io.vertx.core.Promise


class PatientService private constructor(
    creationPromise: Promise<PatientService>
) : AbstractPatientService("res/mongo/configTrauma.json") {
    override var patientCollection: String = "traumaPatient"
    override var busAddrSuffix: String = ""
}