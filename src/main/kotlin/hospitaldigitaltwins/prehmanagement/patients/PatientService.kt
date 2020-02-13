package hospitaldigitaltwins.prehmanagement.patients

import digitaltwinframework.coreimplementation.utils.eventbusutils.FailureCode.PROBLEM_WITH_MONGODB
import digitaltwinframework.coreimplementation.utils.eventbusutils.JsonResponse
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages
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
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

class PatientService(private val missionId: Int) {
    lateinit var mongoClient: MongoClient
    private var patientCollection = "patients"
    private lateinit var vitalParametersCollection: String
    private lateinit var maneuversCollection: String
    private lateinit var administrationsCollection: String
    private val searchQuery = JsonObject()

    private var patientId: String? = null

    val patient: Promise<Patient>
        get() {
            var promise: Promise<Patient> = Promise.promise()
            mongoClient.find(patientCollection, searchQuery) { res ->
                when {
                    res.succeeded() -> {
                        var jobjRes = JsonObject(res.result().get(0).toString())
                        jobjRes.remove("_id")
                        var result = jobjRes.mapTo(Patient::class.java)
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

    init {
        val config = JsonObject()
        Vertx.currentContext().owner().fileSystem().readFile("res/mongo/configPreH.json") { result ->
            when {
                result.succeeded() -> {
                    config.mergeIn(JsonObject(result.result().toString()))
                    mongoClient = MongoClient.createShared(Vertx.currentContext().owner(), config)
                    this.createPatient()
                }
                else -> println(result.cause())
            }
        }
    }

    private fun createPatient() {
        var emptyPatient = JsonObject.mapFrom(MongoPatient())
        mongoClient.save(patientCollection, emptyPatient) { res ->
            when {
                res.succeeded() -> {
                    this.patientId = res.result()

                    vitalParametersCollection = "vitalParametersMeasurementOf" + this.patientId
                    maneuversCollection = "maneuversCollectionOf" + this.patientId
                    administrationsCollection = "administrationsCollectionOf" + this.patientId

                    searchQuery.put("_id", patientId)
                    println("Saved patient with id ${patientId}")
                }
                else -> res.cause().printStackTrace()
            }
        }
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

    private fun updateField(newField: JsonObject): Promise<String> {
        var promise: Promise<String> = Promise.promise()
        var update = JsonObject()
        update.put("\$set", newField)
        mongoClient.updateCollection(patientCollection, searchQuery, update) { res ->
            when {
                res.succeeded() -> promise.complete()
                res.failed() -> promise.fail(res.cause())
                else -> promise.fail("Error during update")
            }
        }
        return promise
    }

    fun registerEventBusConsumers(eb: EventBus) {
        eb.consumer<JsonObject>(PatientOperationIds.GET_PATIENT + missionId) { message ->
            this.patient.future().onComplete(onPromiseCompleteHandler<Patient>(message))
        }

        eb.consumer<JsonObject>(PatientOperationIds.GET_MEDICAL_HISTORY + missionId) { message ->
            this.getMedicalHistory().future().onComplete(onPromiseCompleteHandler<MedicalHistory>(message))
        }

        eb.consumer<JsonObject>(PatientOperationIds.GET_ANAGRAPHIC + missionId) { message ->
            this.getAnagraphic().future().onComplete(onPromiseCompleteHandler<Anagraphic>(message))
        }

        eb.consumer<JsonObject>(PatientOperationIds.GET_STATUS + missionId) { message ->
            this.getStatus().future().onComplete(onPromiseCompleteHandler<PatientState>(message))
        }

        eb.consumer<JsonArray>(PatientOperationIds.GET_VITALPARAMETERS_HISTORY + missionId) { message ->
            this.getAllVitalParametersHistory().future().onComplete { ar ->
                when {
                    ar.succeeded() -> message.reply(ar.result())
                    else -> message.fail(PROBLEM_WITH_MONGODB, ar.cause().toString())
                }
            }
        }

        eb.consumer<JsonArray>(PatientOperationIds.ADD_VITALPARAMETERS + missionId) { message ->
            val addingFuture = message.body().map { addVitalPatameter(it as JsonObject).future() }.toList()
            CompositeFuture.all(addingFuture).onComplete {
                message.reply(JsonObject.mapFrom(JsonResponse(StandardMessages.OPERATION_EXECUTED_MESSAGE)))
            }
        }
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

    /* fun setMedicalHistory(value: MedicalHistory) : Promise<String> {

     }*/

    fun getAnagraphic(): Promise<Anagraphic> {
        return executeDistinctQuery("anagraphic", Anagraphic::class.java)
    }

    /*fun	setAnagraphic(value: Anagraphic): Promise<String>{

    }*/

    fun getStatus(): Promise<PatientState> {
        return executeDistinctQuery("status", PatientState::class.java)
    }

    /*fun setStatus(value: PatientState) : Promise<String>{

    }*/

    fun getAllVitalParametersHistory(): Promise<JsonArray> {
        var promise: Promise<JsonArray> = Promise.promise()
        var emptyQuery = json { obj() }
        patientId?.let {
            mongoClient.find(vitalParametersCollection, emptyQuery) { res ->
                when {
                    res.succeeded() -> {
                        var jArrayRes = JsonArray(res.result())
                        jArrayRes.forEach { (it as JsonObject).remove("_id") }
                        promise.complete(jArrayRes)
                    }
                    else -> {
                        println(res.cause())
                        promise.fail(res.cause())
                    }
                }
            }
        } ?: promise.fail(IllegalStateException())
        return promise
    }

    fun addVitalPatameter(vitalParameter: JsonObject): Promise<String> {
        var promise: Promise<String> = Promise.promise()
        patientId?.let {
            mongoClient.save(vitalParametersCollection, vitalParameter) { res ->
                when {
                    res.succeeded() -> {
                        val parameterID = res.result()
                        println("Saved vital parameter with id ${parameterID}")
                        promise.complete()
                    }
                    else -> {
                        res.cause().printStackTrace()
                        println(res.cause())
                        promise.fail(res.cause())
                    }
                }
            }
        } ?: promise.fail(IllegalStateException())
        return promise
    }


}