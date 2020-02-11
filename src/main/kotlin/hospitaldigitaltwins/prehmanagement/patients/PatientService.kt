package hospitaldigitaltwins.prehmanagement.patients

import digitaltwinframework.coreimplementation.utils.eventbusutils.FailureCode.PROBLEM_WITH_MONGODB
import hospitaldigitaltwins.ontologies.MedicalHistory
import hospitaldigitaltwins.ontologies.Patient
import io.vertx.core.AsyncResult
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import io.vertx.kotlin.core.json.get

class PatientService(private val missionId: Int) {
    lateinit var mongoClient: MongoClient
    private var collection = "patient"
    private val searchQuery = JsonObject()

    private lateinit var patientId: String

    val patient: Promise<Patient>
        get() {
            var promise: Promise<Patient> = Promise.promise()
            mongoClient.find(collection, searchQuery) { res ->
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

    var medicalHistory: Promise<MedicalHistory>
        get() {
            var promise: Promise<MedicalHistory> = Promise.promise()
            mongoClient.distinctWithQuery(
                collection,
                "medicalHistory",
                JsonObject::class.java.name,//String::class.qualifiedName,
                searchQuery
            ) { res ->

                when {
                    res.succeeded() -> {
                        var jobjRes = res.result().get<JsonObject>(0)
                        var result = jobjRes.mapTo(MedicalHistory::class.java)
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
        set(value) {

        }
/*
	var anagraphic: Anagraphic
		get() {

		}
		set(value){

		}

	var status: Status
		get() {

		}
		set(value){

		}
*/

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
        var emptyPatient = JsonObject.mapFrom(Patient())
        mongoClient.save(collection, emptyPatient) { res ->
            when {
                res.succeeded() -> {
                    this.patientId = res.result()
                    searchQuery.put("_id", patientId)
                    println("Saved patient with id ${patientId}")
                }
                else -> res.cause().printStackTrace()
            }
        }
    }

    fun registerEventBusConsumers(eb: EventBus) {
        eb.consumer<JsonObject>(PatientOperationIds.GET_PATIENT + missionId) { message ->
            this.patient.future().onComplete { ar: AsyncResult<Patient> ->
                when {
                    ar.succeeded() -> message.reply(JsonObject.mapFrom(ar.result()))
                    else -> message.fail(
                        PROBLEM_WITH_MONGODB,
                        ar.cause().toString()
                    )
                }
            }
        }

        eb.consumer<JsonObject>(PatientOperationIds.GET_MEDICAL_HISTORY + missionId) { message ->
            this.medicalHistory.future().onComplete { ar: AsyncResult<MedicalHistory> ->
                when {
                    ar.succeeded() -> message.reply(JsonObject.mapFrom(ar.result()))
                    else -> message.fail(
                        PROBLEM_WITH_MONGODB,
                        ar.cause().toString()
                    )
                }
            }
        }
    }
}