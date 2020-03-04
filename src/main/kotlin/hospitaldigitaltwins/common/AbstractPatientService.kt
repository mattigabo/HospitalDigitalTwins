package hospitaldigitaltwins.common

import digitaltwinframework.coreimplementation.utils.eventbusutils.FailureCode
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

/**
 * Created by Matteo Gabellini on 04/03/2020.
 */
abstract class AbstractPatientService(var mongoConfigPath: String) {
    lateinit var mongoClient: MongoClient

    abstract var patientCollection: String
    abstract var busAddrSuffix: String
    private var basicPatientInitPromise: Promise<AbstractPatientService> = Promise.promise()
    protected val basicPatientInitFuture: Future<AbstractPatientService> = basicPatientInitPromise.future()

    protected lateinit var vitalParametersManagement: VitalParametersManagement
    protected lateinit var administrationsManagement: AdministrationsManagement
    protected lateinit var maneuversManagement: ManeuversManagement

    protected val emptySearchQuery = JsonObject()


    var patientId: String? = null
        private set

    val patient: Promise<Patient>
        get() {
            var promise: Promise<Patient> = Promise.promise()
            var vitalParFuture = this.vitalParametersManagement.getCurrentVitalParameters()
            var administrationFuture = this.administrationsManagement.getAllAdministration()
            var maneuverFuture = this.maneuversManagement.getExecutedManeuver()
            CompositeFuture.all(vitalParFuture, administrationFuture, maneuverFuture).onComplete {
                var vitalPar = it.result().resultAt<JsonArray>(0)
                var administrations = it.result().resultAt<JsonArray>(1)
                var maneuvers = it.result().resultAt<JsonArray>(2)
                mongoClient.find(patientCollection, emptySearchQuery) { res ->
                    when {
                        res.succeeded() -> {
                            var jobjRes = JsonObject(res.result().get(0).toString())
                            jobjRes.remove("_id")
                            var partialResult = jobjRes.mapTo(MongoPatient::class.java)
                            var resultList = JsonArray()
                            var result = Patient(
                                partialResult.anagraphic,
                                partialResult.medicalHistory,
                                partialResult.status,
                                vitalPar,
                                maneuvers,
                                administrations
                            )
                            promise.complete(result)
                        }
                        else -> {
                            println(res.cause())
                            promise.fail(res.cause())
                        }
                    }
                }
            }
            return promise
        }

    init {
        val config = JsonObject()
        Vertx.currentContext().owner().fileSystem().readFile(mongoConfigPath) { result ->
            when {
                result.succeeded() -> {
                    config.mergeIn(JsonObject(result.result().toString()))
                    mongoClient = MongoClient.createShared(Vertx.currentContext().owner(), config)
                    this.createPatientOnMongo().future().onComplete {
                        println(it)
                        basicPatientInitPromise.complete(this)
                    }
                }
                else -> basicPatientInitPromise.fail(result.cause())
            }
        }
    }

    private fun createPatientOnMongo(): Promise<String> {
        val mongoCreationPromise = Promise.promise<String>()
        var emptyPatient = JsonObject.mapFrom(MongoPatient())
        mongoClient.save(patientCollection, emptyPatient) { res ->
            when {
                res.succeeded() -> {
                    this.patientId = res.result()

                    var vitalParamCollection = "vitalParametersMeasurementOf" + this.patientId
                    var administrationCollection = "administrationsCollectionOf" + this.patientId
                    var maneuversCollection = "maneuversCollectionOf" + this.patientId
                    vitalParametersManagement =
                        VitalParametersManagement(
                            this,
                            vitalParamCollection
                        )
                    maneuversManagement =
                        ManeuversManagement(this, maneuversCollection)
                    administrationsManagement =
                        AdministrationsManagement(
                            this,
                            administrationCollection
                        )

                    emptySearchQuery.put("_id", patientId)
                    mongoCreationPromise.complete("Saved patient with id ${patientId}")
                }
                else -> mongoCreationPromise.fail(res.cause())
            }
        }
        return mongoCreationPromise
    }

    private fun <T> executeDistinctQuery(fieldName: String, destinationClass: Class<T>): Promise<T> {
        var promise: Promise<T> = Promise.promise()
        mongoClient.distinctWithQuery(
            patientCollection,
            fieldName,
            JsonObject::class.java.name,
            emptySearchQuery
        ) { res ->
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
        mongoClient.updateCollection(patientCollection, emptySearchQuery, update) { res ->
            when {
                res.succeeded() -> promise.complete("Update complete!")
                res.failed() -> promise.fail(res.cause())
                else -> promise.fail("Error during update")
            }
        }
        return promise.future()
    }

    fun registerEventBusConsumers(eb: EventBus) {
        eb.consumer<JsonObject>(PatientOperationIds.GET_PATIENT + busAddrSuffix) { message ->
            this.patient.future().onComplete(onPromiseCompleteHandler<Patient>(message))
        }

        eb.consumer<JsonObject>(PatientOperationIds.GET_MEDICAL_HISTORY + busAddrSuffix) { message ->
            this.getMedicalHistory().future().onComplete(onPromiseCompleteHandler<MedicalHistory>(message))
        }

        eb.consumer<JsonObject>(PatientOperationIds.UPDATE_MEDICAL_HISTORY + busAddrSuffix) { message ->
            val medicalHistory = message.body().mapTo(MedicalHistory::class.java)
            this.setMedicalHistory(medicalHistory).onComplete {
                message.reply(JsonObject.mapFrom(JsonResponse(it.result())))
            }.onFailure {
                message.fail(FailureCode.PROBLEM_IN_PATIENT_FIELD_UPDATE, it.toString())
            }
        }

        eb.consumer<JsonObject>(PatientOperationIds.GET_ANAGRAPHIC + busAddrSuffix) { message ->
            this.getAnagraphic().future().onComplete(onPromiseCompleteHandler<Anagraphic>(message))
        }

        eb.consumer<JsonObject>(PatientOperationIds.UPDATE_ANAGRAPHIC + busAddrSuffix) { message ->
            val anagraphic = message.body().mapTo(Anagraphic::class.java)
            this.setAnagraphic(anagraphic).onComplete {
                message.reply(JsonObject.mapFrom(JsonResponse(it.result())))
            }.onFailure {
                message.fail(FailureCode.PROBLEM_IN_PATIENT_FIELD_UPDATE, it.toString())
            }
        }

        eb.consumer<JsonObject>(PatientOperationIds.GET_STATUS + busAddrSuffix) { message ->
            this.getStatus().future().onComplete(onPromiseCompleteHandler<PatientState>(message))
        }

        eb.consumer<JsonObject>(PatientOperationIds.UPDATE_STATUS + busAddrSuffix) { message ->
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
                else -> message.fail(FailureCode.PROBLEM_WITH_MONGODB, ar.cause().toString())
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

}