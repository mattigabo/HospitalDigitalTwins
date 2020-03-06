package hospitaldigitaltwins.traumamanagement.patient

import digitaltwinframework.coreimplementation.restmanagement.CoreManagementApiRESTAdapter.Companion.OperationIDS.GET_ALL_LINK_TO_OTHER_DT
import digitaltwinframework.coreimplementation.restmanagement.EventBusRestRequestForwarder
import digitaltwinframework.coreimplementation.utils.eventbusutils.FailureCode
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages
import hospitaldigitaltwins.common.AbstractPatientService
import hospitaldigitaltwins.common.PatientOperationIds
import hospitaldigitaltwins.ontologies.Anagraphic
import hospitaldigitaltwins.ontologies.MedicalHistory
import hospitaldigitaltwins.prehmanagement.ontologies.PatientState
import hospitaldigitaltwins.traumamanagement.ManageAPatientFrom
import hospitaldigitaltwins.traumamanagement.ProcessRelation
import io.vertx.core.CompositeFuture
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

            if (preHRelation == null) {
                loadPreHRelation().onComplete { ar ->
                    requestPatientInfoToPreHDT().onComplete { ar ->
                        when {
                            ar.succeeded() -> message.reply(ar.result())
                            else -> message.fail(FailureCode.PROBLEM_IN_DT_COMMUNICATION, ar.cause().toString())
                        }
                    }.onFailure { message.fail(FailureCode.PROBLEM_IN_DT_COMMUNICATION, it.toString()) }
                }.onFailure { message.fail(FailureCode.PROBLEM_IN_DT_COMMUNICATION, it.toString()) }
            } else {
                requestPatientInfoToPreHDT().onComplete { ar ->
                    when {
                        ar.succeeded() -> message.reply(ar.result())
                        else -> message.fail(FailureCode.PROBLEM_IN_DT_COMMUNICATION, ar.cause().toString())
                    }
                }.onFailure { message.fail(FailureCode.PROBLEM_IN_DT_COMMUNICATION, it.toString()) }
            }
        }

        eb.consumer<String>(PATIENT_TAKEN) {
            patientConsumer.unregister()
            this.registerEventBusConsumers(eb)
            loadPreHRelation().onComplete { ar ->
                this.loadPatientInfoOnTraumaDB()
            }.onFailure { println("PreH Information not loaded! Start from a new Patient Data") }
        }
    }

    private fun loadPreHRelation(): Future<String> {
        val patientPromise = Promise.promise<String>()
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
                    patientPromise.complete("Relation loaded!")
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

    private fun loadPatientInfoOnTraumaDB(): Future<String> {
        val loadingPromise = Promise.promise<String>()
        CompositeFuture.all(
            loadPatientAnagraphic(),
            loadPatientStatus(),
            loadPatientMedicalHistory(),
            loadPatientVitalParameters()
        ).onComplete {
            loadingPromise.complete("Loading from PreH DT completed")
        }
        return loadingPromise.future()
    }

    private fun loadPatientAnagraphic(): Future<String> {
        val loadingPromise = Promise.promise<String>()
        preHRelation!!.let {
            restClient.get(it.restPort, it.restHost, "/missions/${it.missionId}/patient/anagraphic")
                .`as`(BodyCodec.jsonObject())
                .send {
                    when {
                        it.succeeded() -> {
                            this.setAnagraphic(it.result().body().mapTo(Anagraphic::class.java)).onComplete {
                                loadingPromise.complete(it.result())
                            }
                        }
                        it.failed() -> loadingPromise.fail(it.cause())
                        else -> loadingPromise.fail("Problem with the communication with the PreHDT")
                    }
                }
        }
        return loadingPromise.future()
    }

    private fun loadPatientStatus(): Future<String> {
        val loadingPromise = Promise.promise<String>()
        preHRelation!!.let {
            restClient.get(it.restPort, it.restHost, "/missions/${it.missionId}/patient/status")
                .`as`(BodyCodec.jsonObject())
                .send {
                    when {
                        it.succeeded() -> {
                            this.setStatus(it.result().body().mapTo(PatientState::class.java)).onComplete {
                                loadingPromise.complete(it.result())
                            }
                        }
                        it.failed() -> loadingPromise.fail(it.cause())
                        else -> loadingPromise.fail("Problem with the communication with the PreHDT")
                    }
                }
        }
        return loadingPromise.future()
    }

    private fun loadPatientMedicalHistory(): Future<String> {
        val loadingPromise = Promise.promise<String>()
        preHRelation!!.let {
            restClient.get(it.restPort, it.restHost, "/missions/${it.missionId}/patient/medicalhistory")
                .`as`(BodyCodec.jsonObject())
                .send {
                    when {
                        it.succeeded() -> {
                            this.setMedicalHistory(it.result().body().mapTo(MedicalHistory::class.java)).onComplete {
                                loadingPromise.complete(it.result())
                            }
                        }
                        it.failed() -> loadingPromise.fail(it.cause())
                        else -> loadingPromise.fail("Problem with the communication with the PreHDT")
                    }
                }
        }
        return loadingPromise.future()
    }

    private fun loadPatientVitalParameters(): Future<String> {
        val loadingPromise = Promise.promise<String>()
        preHRelation!!.let {
            restClient
                .get(it.restPort, it.restHost, "/missions/${it.missionId}/patient/vital-parameters/history")
                .`as`(BodyCodec.jsonArray())
                .send {
                    when {
                        it.succeeded() -> {
                            val futures = ArrayList<Future<String>>()
                            (it.result().body() as JsonArray).forEach { vitalParameter ->
                                futures.add(
                                    this.vitalParametersManagement.addVitalPatameter(vitalParameter as JsonObject)
                                )
                            }
                            CompositeFuture.all(futures.toList()).onComplete {
                                loadingPromise.complete()
                            }
                        }
                        it.failed() -> loadingPromise.fail(it.cause())
                        else -> loadingPromise.fail("Problem with the communication with the PreHDT")
                    }
                }
        }
        return loadingPromise.future()
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


