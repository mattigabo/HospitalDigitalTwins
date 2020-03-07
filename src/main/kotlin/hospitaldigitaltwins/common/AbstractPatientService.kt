package hospitaldigitaltwins.common

import digitaltwinframework.coreimplementation.utils.eventbusutils.FailureCode
import digitaltwinframework.coreimplementation.utils.eventbusutils.JsonResponse
import hospitaldigitaltwins.ontologies.Anagraphic
import hospitaldigitaltwins.ontologies.MedicalHistory
import hospitaldigitaltwins.ontologies.MongoPatient
import hospitaldigitaltwins.ontologies.Patient
import hospitaldigitaltwins.prehmanagement.ontologies.PatientState
import io.vertx.core.CompositeFuture
import io.vertx.core.Future
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient

/**
 * Created by Matteo Gabellini on 04/03/2020.
 */
abstract class AbstractPatientService(mongoConfigPath: String) : AbstractMongoClientService(mongoConfigPath) {

    open var busAddrSuffix: String = ""
    private var basicPatientInitPromise: Promise<AbstractPatientService> = Promise.promise()
    protected val basicPatientInitFuture: Future<AbstractPatientService> = basicPatientInitPromise.future()

    protected lateinit var vitalParametersManagement: VitalParametersManagement
    protected lateinit var administrationsManagement: AdministrationsManagement
    protected lateinit var maneuversManagement: ManeuversManagement

    protected var updateAPIConsumers = ArrayList<MessageConsumer<out Any>>()

    var patientId: String? = null
        private set

    val patient: Promise<Patient>
        get() {
            var promise: Promise<Patient> = Promise.promise()
            var anagraphicFuture = this.getAnagraphic()
            var medicalHistoryFuture = this.getMedicalHistory()
            var statusFuture = this.getStatus()
            var vitalParFuture = this.vitalParametersManagement.getCurrentVitalParameters()
            var administrationFuture = this.administrationsManagement.getAllAdministration()
            var maneuverFuture = this.maneuversManagement.getExecutedManeuver()
            CompositeFuture.all(
                anagraphicFuture,
                medicalHistoryFuture,
                statusFuture,
                vitalParFuture,
                administrationFuture,
                maneuverFuture
            ).onSuccess {
                var anagraphic = it.resultAt<Anagraphic>(0)
                var medicalHistory = it.resultAt<MedicalHistory>(1)
                var status = it.resultAt<PatientState>(2)
                var vitalPar = it.resultAt<JsonArray>(3)
                var administrations = it.resultAt<JsonArray>(4)
                var maneuvers = it.resultAt<JsonArray>(5)
                var result = Patient(
                    anagraphic,
                    medicalHistory,
                    status,
                    vitalPar,
                    maneuvers,
                    administrations
                )
                promise.complete(result)
            }.onFailure {
                println(it)
                promise.fail(it)
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
                    this.createPatientOnMongo().future().onSuccess {
                        println(it)
                        basicPatientInitPromise.complete(this)
                    }.onFailure { basicPatientInitPromise.fail(it) }
                }
                else -> basicPatientInitPromise.fail(result.cause())
            }
        }
    }

    private fun createPatientOnMongo(): Promise<String> {
        val mongoCreationPromise = Promise.promise<String>()
        var emptyPatient = JsonObject.mapFrom(MongoPatient())
        mongoClient.save(collection, emptyPatient) { res ->
            when {
                res.succeeded() -> {
                    this.patientId = res.result()

                    var vitalParamCollection = "vitalParametersMeasurementOf" + this.patientId
                    var administrationCollection = "administrationsCollectionOf" + this.patientId
                    var maneuversCollection = "maneuversCollectionOf" + this.patientId
                    vitalParametersManagement = VitalParametersManagement(this, vitalParamCollection)
                    maneuversManagement = ManeuversManagement(this, maneuversCollection)
                    administrationsManagement = AdministrationsManagement(this, administrationCollection)

                    emptySearchQuery.put("_id", patientId)
                    mongoCreationPromise.complete("Saved patient with id ${patientId}")
                }
                else -> mongoCreationPromise.fail(res.cause())
            }
        }
        return mongoCreationPromise
    }

    open fun registerEventBusConsumers(eb: EventBus) {
        eb.consumer<JsonObject>(PatientOperationIds.GET_PATIENT + busAddrSuffix) { message ->
            this.patient.future().onComplete(onOperationCompleteHandler<Patient>(message))
        }

        eb.consumer<JsonObject>(PatientOperationIds.GET_MEDICAL_HISTORY + busAddrSuffix) { message ->
            this.getMedicalHistory().onComplete(onOperationCompleteHandler<MedicalHistory>(message))
        }

        val setMedHistoryCons =
            eb.consumer<JsonObject>(PatientOperationIds.UPDATE_MEDICAL_HISTORY + busAddrSuffix) { message ->
                val medicalHistory = message.body().mapTo(MedicalHistory::class.java)
                this.setMedicalHistory(medicalHistory).onSuccess {
                    message.reply(JsonObject.mapFrom(JsonResponse(it)))
                }.onFailure {
                    message.fail(FailureCode.PROBLEM_IN_PATIENT_FIELD_UPDATE, it.toString())
                }
            }
        updateAPIConsumers.add(setMedHistoryCons)

        eb.consumer<JsonObject>(PatientOperationIds.GET_ANAGRAPHIC + busAddrSuffix) { message ->
            this.getAnagraphic().onComplete(onOperationCompleteHandler<Anagraphic>(message))
        }

        val updateAnagraphicConsumer =
            eb.consumer<JsonObject>(PatientOperationIds.UPDATE_ANAGRAPHIC + busAddrSuffix) { message ->
                val anagraphic = message.body().mapTo(Anagraphic::class.java)
                this.setAnagraphic(anagraphic).onSuccess {
                    message.reply(JsonObject.mapFrom(JsonResponse(it)))
                }.onFailure {
                    message.fail(FailureCode.PROBLEM_IN_PATIENT_FIELD_UPDATE, it.toString())
                }
            }
        updateAPIConsumers.add(updateAnagraphicConsumer)

        eb.consumer<JsonObject>(PatientOperationIds.GET_STATUS + busAddrSuffix) { message ->
            this.getStatus().onComplete(onOperationCompleteHandler<PatientState>(message))
        }

        val updateStatusConsumer =
            eb.consumer<JsonObject>(PatientOperationIds.UPDATE_STATUS + busAddrSuffix) { message ->
                val patientState = message.body().mapTo(PatientState::class.java)
                this.setStatus(patientState).onSuccess {
                    message.reply(JsonObject.mapFrom(JsonResponse(it)))
                }.onFailure {
                    message.fail(FailureCode.PROBLEM_IN_PATIENT_FIELD_UPDATE, it.toString())
                }
            }
        updateAPIConsumers.add(updateStatusConsumer)

        this.vitalParametersManagement.registerEventBusConsumers(eb)
        this.administrationsManagement.registerEventBusConsumers(eb)
        this.maneuversManagement.registerEventBusConsumers(eb)
    }

    fun getMedicalHistory(): Future<MedicalHistory> {
        return executeDistinctQuery("medicalHistory", MedicalHistory::class.java)
    }

    fun setMedicalHistory(value: MedicalHistory): Future<String> {
        return this.updateField("medicalHistory", JsonObject.mapFrom(value))
    }

    fun getAnagraphic(): Future<Anagraphic> {
        return executeDistinctQuery("anagraphic", Anagraphic::class.java)
    }

    fun setAnagraphic(value: Anagraphic): Future<String> {
        return this.updateField("anagraphic", JsonObject.mapFrom(value))
    }

    fun getStatus(): Future<PatientState> {
        return executeDistinctQuery("status", PatientState::class.java)
    }

    fun setStatus(value: PatientState): Future<String> {
        return this.updateField("status", JsonObject.mapFrom(value))
    }

}