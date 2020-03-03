package hospitaldigitaltwins.prehmanagement.patients

import digitaltwinframework.coreimplementation.utils.eventbusutils.FailureCode
import digitaltwinframework.coreimplementation.utils.eventbusutils.JsonResponse
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages
import io.vertx.core.Promise
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

/**
 * Created by Matteo Gabellini on 20/02/2020.
 */
class AdministrationsManagement(var patientService: PatientService, val administrationCollection: String) {

    fun registerEventBusConsumers(eb: EventBus) {
        eb.consumer<JsonArray>(PatientOperationIds.GET_ALL_ADMINISTRATION + patientService.missionId) { message ->
            this.getAllAdministration().future().onComplete { ar ->
                when {
                    ar.succeeded() -> message.reply(ar.result())
                    else -> message.fail(FailureCode.PROBLEM_WITH_MONGODB, ar.cause().toString())
                }
            }
        }

        eb.consumer<JsonObject>(PatientOperationIds.ADD_ADMINISTRATION + patientService.missionId) { message ->
            val addingFuture = addAdministration(message.body()).future()
            addingFuture.onComplete {
                message.reply(JsonObject.mapFrom(JsonResponse(StandardMessages.OPERATION_EXECUTED_MESSAGE)))
            }
        }
    }


    fun getAllAdministration(): Promise<JsonArray> {
        var promise: Promise<JsonArray> = Promise.promise()
        var emptyQuery = json { obj() }
        patientService.patientId?.let {
            patientService.mongoClient.find(administrationCollection, emptyQuery) { res ->
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

    fun addAdministration(administration: JsonObject): Promise<String> {
        var promise: Promise<String> = Promise.promise()
        patientService.patientId?.let {
            patientService.mongoClient.save(administrationCollection, administration) { res ->
                when {
                    res.succeeded() -> {
                        val parameterID = res.result()
                        println("Saved administration with id ${parameterID}")
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