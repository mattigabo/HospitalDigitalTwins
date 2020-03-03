package hospitaldigitaltwins.prehmanagement.patients

import digitaltwinframework.coreimplementation.utils.eventbusutils.FailureCode
import digitaltwinframework.coreimplementation.utils.eventbusutils.JsonResponse
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages
import io.vertx.core.Future
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
class ManeuversManagement(var patientService: PatientService, val maneuversCollection: String) {

    fun registerEventBusConsumers(eb: EventBus) {
        eb.consumer<JsonArray>(PatientOperationIds.GET_EXECUTED_MANEUVERS + patientService.missionId) { message ->
            this.getExecutedManeuver().onComplete { ar ->
                when {
                    ar.succeeded() -> message.reply(ar.result())
                    else -> message.fail(FailureCode.PROBLEM_WITH_MONGODB, ar.cause().toString())
                }
            }
        }

        eb.consumer<String>(PatientOperationIds.GET_TIMED_MANEUVER + patientService.missionId) { message ->
            val maneuverId = message.body()
            this.getTimedManeuver(maneuverId).onComplete { ar ->
                when {
                    ar.succeeded() -> message.reply(ar.result())
                    else -> message.fail(FailureCode.PROBLEM_WITH_MONGODB, ar.cause().toString())
                }
            }
        }

        eb.consumer<JsonObject>(PatientOperationIds.ADD_MANEUVER + patientService.missionId) { message ->
            val addingFuture = addManeuver(message.body())
            addingFuture.onComplete {
                message.reply(JsonObject.mapFrom(JsonResponse(StandardMessages.OPERATION_EXECUTED_MESSAGE)))
            }
        }

        eb.consumer<JsonObject>(PatientOperationIds.ADD_TIMED_MANEUVER + patientService.missionId) { message ->
            val addingFuture = addTimedManeuver(message.body())
            addingFuture.onComplete {
                val response = JsonObject()
                response.put("maneuverId", it.result())
                message.reply(response)
            }
        }

        eb.consumer<JsonObject>(PatientOperationIds.UPDATE_TIMED_MANEUVER + patientService.missionId) { message ->
            val missionId = message.body().getString("maneuverId")
            val updatedManeuver = message.body().copy()
            updatedManeuver.remove("maneuverId")
            val updatingFuture = updateTimedManeuver(missionId, updatedManeuver)
            updatingFuture.onComplete {
                message.reply(JsonObject.mapFrom(JsonResponse(StandardMessages.OPERATION_EXECUTED_MESSAGE)))
            }
        }

    }

    fun getExecutedManeuver(): Future<JsonArray> {
        var promise: Promise<JsonArray> = Promise.promise()
        var emptyQuery = json { obj() }
        patientService.patientId?.let {
            patientService.mongoClient.find(maneuversCollection, emptyQuery) { res ->
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
        return promise.future()
    }

    fun addManeuver(maneuver: JsonObject): Future<String> {
        var promise: Promise<String> = Promise.promise()
        patientService.patientId?.let {
            patientService.mongoClient.save(maneuversCollection, maneuver) { res ->
                when {
                    res.succeeded() -> {
                        val parameterID = res.result()
                        println("Saved maneuver with id ${parameterID}")
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
        return promise.future()
    }

    fun addTimedManeuver(timedManuver: JsonObject): Future<String> {
        var promise: Promise<String> = Promise.promise()
        patientService.patientId?.let {
            patientService.mongoClient.save(maneuversCollection, timedManuver) { res ->
                when {
                    res.succeeded() -> {
                        val parameterID = res.result()
                        println("Saved maneuver with id ${parameterID}")
                        promise.complete(parameterID)
                    }
                    else -> {
                        res.cause().printStackTrace()
                        println(res.cause())
                        promise.fail(res.cause())
                    }
                }
            }
        } ?: promise.fail(IllegalStateException())
        return promise.future()
    }

    fun getTimedManeuver(maneuverId: String): Future<JsonObject> {
        var promise: Promise<JsonObject> = Promise.promise()
        var searchField = json { obj("_id" to maneuverId) }
        var findOption = FindOptions()
        patientService.patientId?.let {
            patientService.mongoClient.find(maneuversCollection, searchField) { res ->
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
        return promise.future()
    }


    fun updateTimedManeuver(maneuverId: String, timedManeuver: JsonObject): Future<String> {
        var promise: Promise<String> = Promise.promise()
        var searchField = json { obj("_id" to maneuverId) }
        var update = JsonObject()
        update.put("\$set", timedManeuver)
        patientService.mongoClient.updateCollection(maneuversCollection, searchField, update) { res ->
            when {
                res.succeeded() -> promise.complete("Update complete!")
                res.failed() -> promise.fail(res.cause())
                else -> promise.fail("Error during update")
            }
        }
        return promise.future()
    }
}