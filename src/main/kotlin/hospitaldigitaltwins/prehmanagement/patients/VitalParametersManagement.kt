package hospitaldigitaltwins.prehmanagement.patients

import digitaltwinframework.coreimplementation.utils.eventbusutils.FailureCode
import digitaltwinframework.coreimplementation.utils.eventbusutils.JsonResponse
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages
import hospitaldigitaltwins.ontologies.VitalParametersNames
import io.vertx.core.CompositeFuture
import io.vertx.core.Promise
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.FindOptions
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

/**
 * Created by Matteo Gabellini on 13/02/2020.
 */
class VitalParametersManagement(var patientService: PatientService, val vitalParametersCollection: String) {

    fun registerEventBusConsumers(eb: EventBus) {
        eb.consumer<JsonArray>(PatientOperationIds.GET_VITALPARAMETERS_HISTORY + patientService.missionId) { message ->
            this.getAllVitalParametersHistory().future().onComplete { ar ->
                when {
                    ar.succeeded() -> message.reply(ar.result())
                    else -> message.fail(FailureCode.PROBLEM_WITH_MONGODB, ar.cause().toString())
                }
            }
        }

        eb.consumer<String>(PatientOperationIds.GET_VITALPARAMETER + patientService.missionId) { message ->
            val vitalParameterName = message.body()
            this.getCurrentVitalParameter(vitalParameterName).future().onComplete { ar ->
                when {
                    ar.succeeded() -> message.reply(ar.result())
                    else -> message.fail(FailureCode.PROBLEM_WITH_MONGODB, ar.cause().toString())
                }
            }
        }

        eb.consumer<JsonArray>(PatientOperationIds.GET_VITALPARAMETERS + patientService.missionId) { message ->
            var resultList: JsonArray = JsonArray()
            this.getCurrentVitalParameters(resultList).onComplete {
                message.reply(resultList)
            }
        }

        eb.consumer<String>(PatientOperationIds.GET_VITALPARAMETER_HISTORY + patientService.missionId) { message ->
            val vitalParameterName = message.body()
            this.getVitalParameterHistory(vitalParameterName).future().onComplete { ar ->
                when {
                    ar.succeeded() -> message.reply(ar.result())
                    else -> message.fail(FailureCode.PROBLEM_WITH_MONGODB, ar.cause().toString())
                }
            }
        }

        eb.consumer<JsonArray>(PatientOperationIds.ADD_VITALPARAMETERS + patientService.missionId) { message ->
            val addingFuture = message.body().map { addVitalPatameter(it as JsonObject).future() }.toList()
            CompositeFuture.join(addingFuture).onComplete {
                message.reply(JsonObject.mapFrom(JsonResponse(StandardMessages.OPERATION_EXECUTED_MESSAGE)))
            }
        }
    }

    fun getCurrentVitalParameters(resultList: JsonArray): CompositeFuture {
        var futures = VitalParametersNames.asNameList().map {
            getCurrentVitalParameter(it).future()
        }
        futures.forEach {
            it.onComplete { res ->
                if (res.result().toString() != "{}") {
                    resultList.add(res.result())
                }
            }
        }
        return CompositeFuture.all(futures)
    }

    fun getCurrentVitalParameter(parameterName: String): Promise<JsonObject> {
        var promise: Promise<JsonObject> = Promise.promise()
        var searchField = json { obj("name" to parameterName) }
        var findOption = FindOptions()
        val sortStrategy = JsonObject()
        sortStrategy.put("acquisitionTime", -1)
        findOption.sort = sortStrategy
        patientService.patientId?.let {
            patientService.mongoClient.findWithOptions(vitalParametersCollection, searchField, findOption) { res ->
                when {
                    res.succeeded() -> {
                        try {
                            var jobjResult = JsonObject(res.result().get(0).toString())
                            jobjResult.remove("_id")
                            promise.complete(jobjResult)
                        } catch (e: IndexOutOfBoundsException) {
                            promise.complete(JsonObject())
                        }
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

    fun getVitalParameterHistory(vitalParameterName: String): Promise<JsonArray> {
        var promise: Promise<JsonArray> = Promise.promise()
        var searchQuery = json { obj("name" to vitalParameterName) }
        patientService.patientId?.let {
            patientService.mongoClient.find(vitalParametersCollection, searchQuery) { res ->
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

    fun getAllVitalParametersHistory(): Promise<JsonArray> {
        var promise: Promise<JsonArray> = Promise.promise()
        var emptyQuery = json { obj() }
        patientService.patientId?.let {
            patientService.mongoClient.find(vitalParametersCollection, emptyQuery) { res ->
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
        patientService.patientId?.let {
            patientService.mongoClient.save(vitalParametersCollection, vitalParameter) { res ->
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