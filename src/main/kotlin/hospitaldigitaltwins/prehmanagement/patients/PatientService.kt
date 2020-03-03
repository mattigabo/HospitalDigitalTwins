package hospitaldigitaltwins.prehmanagement.patients

import digitaltwinframework.coreimplementation.utils.eventbusutils.FailureCode
import digitaltwinframework.coreimplementation.utils.eventbusutils.FailureCode.PROBLEM_WITH_MONGODB
import digitaltwinframework.coreimplementation.utils.eventbusutils.JsonResponse
import hospitaldigitaltwins.ontologies.Anagraphic
import hospitaldigitaltwins.ontologies.MedicalHistory
import hospitaldigitaltwins.ontologies.MongoPatient
import hospitaldigitaltwins.ontologies.Patient
import hospitaldigitaltwins.prehmanagement.ontologies.PatientState
import io.vertx.core.*
import io.vertx.core.eventbus.EventBus
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import io.vertx.kotlin.core.json.get

class PatientService private constructor(val missionId: Int, creationPromise: Promise<PatientService>) {
    lateinit var mongoClient: MongoClient
    private var patientCollection = "patients"
    private lateinit var vitalParametersManagement: VitalParametersManagement
    private lateinit var maneuversCollection: String
    private lateinit var administrationsManagement: AdministrationsManagement
    private lateinit var maneuversManagement: ManeuversManagement
    private val searchQuery = JsonObject()

    var patientId: String? = null
        private set

    val patient: Promise<Patient>
        get() {
            var promise: Promise<Patient> = Promise.promise()
            mongoClient.find(patientCollection, searchQuery) { res ->
                when {
                    res.succeeded() -> {
                        var jobjRes = JsonObject(res.result().get(0).toString())
                        jobjRes.remove("_id")
                        var partialResult = jobjRes.mapTo(MongoPatient::class.java)
                        var resultList = JsonArray()
                        this.vitalParametersManagement.getCurrentVitalParameters().onComplete {
                            var result = Patient(
                                partialResult.anagraphic,
                                partialResult.medicalHistory,
                                partialResult.status,
                                ArrayList(),
                                ArrayList(),
                                ArrayList()
                            )
                            promise.complete(result)
                        }
                    }
                    else -> {
                        println(res.cause())
                        promise.fail(res.cause())
                    }
                }
            }
            return promise
        }

    init {
        val config = JsonObject()
        Vertx.currentContext().owner().fileSystem().readFile("res/mongo/configPreH.json") { result ->
            when {
                result.succeeded() -> {
                    config.mergeIn(JsonObject(result.result().toString()))
                    mongoClient = MongoClient.createShared(Vertx.currentContext().owner(), config)
                    this.createPatient().future().onComplete {
                        println(it)
                        creationPromise.complete(this)
                    }
                }
                else -> creationPromise.fail(result.cause())
            }
        }
    }

    private fun createPatient(): Promise<String> {
        val mongoCreationPromise = Promise.promise<String>()
        var emptyPatient = JsonObject.mapFrom(MongoPatient())
        mongoClient.save(patientCollection, emptyPatient) { res ->
            when {
                res.succeeded() -> {
                    this.patientId = res.result()

                    var vitalParamCollection = "vitalParametersMeasurementOf" + this.patientId
                    var administrationCollection = "administrationsCollectionOf" + this.patientId
                    var maneuversCollection = "maneuversCollectionOf" + this.patientId
                    vitalParametersManagement = VitalParametersManagement(this, vitalParamCollection)
                    maneuversManagement = ManeuversManagement(this, maneuversCollection)
                    administrationsManagement = AdministrationsManagement(this, administrationCollection)

                    searchQuery.put("_id", patientId)
                    mongoCreationPromise.complete("Saved patient with id ${patientId}")
                }
                else -> mongoCreationPromise.fail(res.cause())
            }
        }
        return mongoCreationPromise
    }

    private fun <T> executeDistinctQuery(fieldName: String, destinationClass: Class<T>): Promise<T> {
        var promise: Promise<T> = Promise.promise()
        mongoClient.distinctWithQuery(patientCollection, fieldName, JsonObject::class.java.name, searchQuery) { res ->
            when {
                res.succeeded() -> {
                    var jobjRes = res.result().get<JsonObject>(0)
                    var result = jobjRes.mapTo(destinationClass)
                    promise.complete(result)
                }
                else -> {
                    println(res.cause())
                    promise.fail(res.cause())
                }
            }
        }
        return promise
    }

    private fun updateField(newField: JsonObject): Future<String> {
        var promise: Promise<String> = Promise.promise()
        var update = JsonObject()
        update.put("\$set", newField)
        mongoClient.updateCollection(patientCollection, searchQuery, update) { res ->
            when {
                res.succeeded() -> promise.complete("Update complete!")
                res.failed() -> promise.fail(res.cause())
                else -> promise.fail("Error during update")
            }
        }
        return promise.future()
    }

    fun registerEventBusConsumers(eb: EventBus) {
        eb.consumer<JsonObject>(PatientOperationIds.GET_PATIENT + missionId) { message ->
            this.patient.future().onComplete(onPromiseCompleteHandler<Patient>(message))
        }

        eb.consumer<JsonObject>(PatientOperationIds.GET_MEDICAL_HISTORY + missionId) { message ->
            this.getMedicalHistory().future().onComplete(onPromiseCompleteHandler<MedicalHistory>(message))
        }

        eb.consumer<JsonObject>(PatientOperationIds.UPDATE_MEDICAL_HISTORY + missionId) { message ->
            val medicalHistory = message.body().mapTo(MedicalHistory::class.java)
            this.setMedicalHistory(medicalHistory).onComplete {
                message.reply(JsonObject.mapFrom(JsonResponse(it.result())))
            }.onFailure {
                message.fail(FailureCode.PROBLEM_IN_PATIENT_FIELD_UPDATE, it.toString())
            }
        }

        eb.consumer<JsonObject>(PatientOperationIds.GET_ANAGRAPHIC + missionId) { message ->
            this.getAnagraphic().future().onComplete(onPromiseCompleteHandler<Anagraphic>(message))
        }

        eb.consumer<JsonObject>(PatientOperationIds.UPDATE_ANAGRAPHIC + missionId) { message ->
            val anagraphic = message.body().mapTo(Anagraphic::class.java)
            this.setAnagraphic(anagraphic).onComplete {
                message.reply(JsonObject.mapFrom(JsonResponse(it.result())))
            }.onFailure {
                message.fail(FailureCode.PROBLEM_IN_PATIENT_FIELD_UPDATE, it.toString())
            }
        }

        eb.consumer<JsonObject>(PatientOperationIds.GET_STATUS + missionId) { message ->
            this.getStatus().future().onComplete(onPromiseCompleteHandler<PatientState>(message))
        }

        eb.consumer<JsonObject>(PatientOperationIds.UPDATE_STATUS + missionId) { message ->
            val patientState = message.body().mapTo(PatientState::class.java)
            this.setStatus(patientState).onComplete {
                message.reply(JsonObject.mapFrom(JsonResponse(it.result())))
            }.onFailure {
                message.fail(FailureCode.PROBLEM_IN_PATIENT_FIELD_UPDATE, it.toString())
            }
        }

        this.vitalParametersManagement.registerEventBusConsumers(eb)
        this.administrationsManagement.registerEventBusConsumers(eb)
        this.maneuversManagement.registerEventBusConsumers(eb)
    }

    private fun <T> onPromiseCompleteHandler(message: Message<JsonObject>): Handler<AsyncResult<T>> {
        return Handler<AsyncResult<T>> { ar: AsyncResult<T> ->
            when {
                ar.succeeded() -> message.reply(JsonObject.mapFrom(ar.result()))
                else -> message.fail(PROBLEM_WITH_MONGODB, ar.cause().toString())
            }
        }
    }

    fun getMedicalHistory(): Promise<MedicalHistory> {
        return executeDistinctQuery("medicalHistory", MedicalHistory::class.java)
    }

    fun setMedicalHistory(value: MedicalHistory): Future<String> {
        val update = JsonObject()
        update.put("medicalHistory", JsonObject.mapFrom(value))
        return this.updateField(update)
    }

    fun getAnagraphic(): Promise<Anagraphic> {
        return executeDistinctQuery("anagraphic", Anagraphic::class.java)
    }

    fun setAnagraphic(value: Anagraphic): Future<String> {
        val update = JsonObject()
        update.put("anagraphic", JsonObject.mapFrom(value))
        return this.updateField(update)
    }

    fun getStatus(): Promise<PatientState> {
        return executeDistinctQuery("status", PatientState::class.java)
    }

    fun setStatus(value: PatientState): Future<String> {
        val update = JsonObject()
        update.put("status", JsonObject.mapFrom(value))
        return this.updateField(update)
    }

    companion object {
        fun createPatient(missionId: Int): Promise<PatientService> {
            val patientPromise = Promise.promise<PatientService>()
            PatientService(missionId, patientPromise)
            return patientPromise
        }
    }
}