package hospitaldigitaltwins.traumamanagement.patient

import digitaltwinframework.coreimplementation.restmanagement.CoreManagementApiRESTAdapter.Companion.OperationIDS.GET_ALL_LINK_TO_OTHER_DT
import digitaltwinframework.coreimplementation.restmanagement.EventBusRestRequestForwarder
import digitaltwinframework.coreimplementation.utils.eventbusutils.FailureCode
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages
import hospitaldigitaltwins.common.AbstractPatientService
import hospitaldigitaltwins.common.PatientOperationIds
import hospitaldigitaltwins.traumamanagement.ManageAPatientFrom
import hospitaldigitaltwins.traumamanagement.ProcessRelation
import io.vertx.core.Future
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.codec.BodyCodec


class PatientService private constructor(
    mongoConfigPath: String,
    creationPromise: Promise<PatientService>
) : AbstractPatientService(mongoConfigPath) {
    override var collection: String = "traumaPatient"

    var restClient = WebClient.create(Vertx.currentContext().owner())
    var preHRelation: ManageAPatientFrom? = null
    var linkRequestUri = "http://localhost:8081/links"

    init {
        super.basicPatientInitFuture.onComplete {
            creationPromise.complete(this)
        }.onFailure {
            creationPromise.fail(it)
        }
    }

    fun registerPatientWaitingConsumer(eb: EventBus) {
        val patientConsumer = eb.consumer<JsonObject>(PatientOperationIds.GET_PATIENT) { message ->
            preHRelation?.let {
                requestPatientInfoToPreHDT().onComplete { ar ->
                    when {
                        ar.succeeded() -> message.reply(ar.result())
                        else -> message.fail(FailureCode.PROBLEM_IN_DT_COMMUNICATION, ar.cause().toString())
                    }
                }.onFailure { message.fail(FailureCode.PROBLEM_IN_DT_COMMUNICATION, it.toString()) }
            } ?: loadAndRequestPatient().onComplete { ar ->
                when {
                    ar.succeeded() -> message.reply(ar.result())
                    else -> message.fail(FailureCode.PROBLEM_IN_DT_COMMUNICATION, ar.cause().toString())
                }
            }.onFailure { message.fail(FailureCode.PROBLEM_IN_DT_COMMUNICATION, it.toString()) }
        }

        eb.consumer<String>(PATIENT_TAKEN) {
            patientConsumer.unregister()
            this.registerEventBusConsumers(eb)
        }
    }

    private fun loadAndRequestPatient(): Future<JsonObject> {
        val patientPromise = Promise.promise<JsonObject>()
        EventBusRestRequestForwarder
            .eventBusInstance()
            .request<JsonArray>(GET_ALL_LINK_TO_OTHER_DT, StandardMessages.EMPTY_MESSAGE) {

                val links = it.result().body()
                links.forEach { link ->
                    if (link is JsonObject) {
                        try {
                            preHRelation = link.mapTo(ProcessRelation::class.java).semantics
                        } catch (e: Exception) {
                            println(e.cause)
                        }
                    }
                }

                preHRelation?.let {
                    requestPatientInfoToPreHDT().onComplete {
                        patientPromise.complete(it.result())
                    }
                } ?: patientPromise.fail("Link to PreH Digital Twin not already loaded")
            }
        return patientPromise.future()
    }

    private fun requestPatientInfoToPreHDT(): Future<JsonObject> {
        val patientPromise = Promise.promise<JsonObject>()
        preHRelation!!.let {
            restClient.get(it.restPort, it.restHost, "/missions/${it.missionId}/patient").`as`(BodyCodec.jsonObject())
                .send {
                    when {
                        it.succeeded() -> patientPromise.complete(it.result().body())
                        it.failed() -> patientPromise.fail(it.cause())
                        else -> patientPromise.fail("Problem with the communication with the PreHDT")
                    }
                }
        }
        return patientPromise.future()
    }

    companion object {
        const val PATIENT_TAKEN = "PatientTaken"

        fun createPatient(mongoConfigPath: String): Promise<PatientService> {
            val patientPromise = Promise.promise<PatientService>()
            PatientService(mongoConfigPath, patientPromise)
            return patientPromise
        }
    }
}


