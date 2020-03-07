package hospitaldigitaltwins.traumamanagement.info

import com.fasterxml.jackson.annotation.JsonProperty
import digitaltwinframework.coreimplementation.restmanagement.EventBusRestRequestForwarder
import digitaltwinframework.coreimplementation.utils.eventbusutils.FailureCode
import digitaltwinframework.coreimplementation.utils.eventbusutils.JsonResponse
import hospitaldigitaltwins.common.AbstractMongoClientService
import hospitaldigitaltwins.traumamanagement.patient.PatientService
import io.vertx.core.Future
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient

/**
 * Created by Matteo Gabellini on 05/03/2020.
 */
class TraumaInfoService(
    mongoConfigPath: String,
    creationPromise: Promise<TraumaInfoService>
) : AbstractMongoClientService(mongoConfigPath) {

    override var collection: String = "traumaInfo"

    var traumaId: String? = null
        private set

    init {
        val config = JsonObject()
        Vertx.currentContext().owner().fileSystem().readFile(mongoConfigPath) { result ->
            when {
                result.succeeded() -> {
                    config.mergeIn(JsonObject(result.result().toString()))
                    mongoClient = MongoClient.createShared(Vertx.currentContext().owner(), config)
                    this.createTraumaOnMongo().future().onComplete {
                        println(it)
                        creationPromise.complete(this)
                    }
                }
                else -> creationPromise.fail(result.cause())
            }
        }
    }

    private fun createTraumaOnMongo(): Promise<String> {
        val mongoCreationPromise = Promise.promise<String>()
        var emptyTrauma = JsonObject.mapFrom(TraumaInfo())
        mongoClient.save(collection, emptyTrauma) { res ->
            when {
                res.succeeded() -> {
                    this.traumaId = res.result()
                    emptySearchQuery.put("_id", traumaId)
                    mongoCreationPromise.complete("Saved Trauma with id ${traumaId}")
                }
                else -> mongoCreationPromise.fail(res.cause())
            }
        }
        return mongoCreationPromise
    }


    fun registerEventBusConsumers(eb: EventBus) {

        eb.consumer<JsonObject>(TraumaInfoOperationIds.GET_BASIC_INFO) { message ->
            this.getBasicInfo().onComplete(onOperationCompleteHandler(message))
        }

        eb.consumer<JsonObject>(TraumaTeamOperationIds.ACTIVATE_TRAUMA_TEAM) { message ->
            val traumaTeamInfo = message.body().mapTo(TraumaTeamInfo::class.java)
            this.activateTraumaTeam(traumaTeamInfo).onComplete {
                message.reply(JsonObject.mapFrom(JsonResponse(it.result())))
            }.onFailure {
                message.fail(FailureCode.PROBLEM_IN_TRAUMA_FIELD_UPDATE, it.toString())
            }
        }

        eb.consumer<JsonObject>(TraumaTeamOperationIds.GET_ACTIVATION_INFO) { message ->
            this.getActivationInfo().onComplete(onOperationCompleteHandler(message))
        }

        eb.consumer<JsonObject>(TraumaTeamOperationIds.TAKE_PATIENT_IN_CHARGE) { message ->
            val psCode = message.body().getInteger("psCode")
            this.takePatientInCharge(psCode).onComplete {
                EventBusRestRequestForwarder
                    .eventBusInstance()
                    .send(PatientService.PATIENT_TAKEN, "")
                message.reply(JsonObject.mapFrom(JsonResponse(it.result())))
            }.onFailure {
                message.fail(FailureCode.PROBLEM_IN_TRAUMA_FIELD_UPDATE, it.toString())
            }
        }
    }

    fun getBasicInfo(): Future<TraumaInfo> {
        var promise: Promise<TraumaInfo> = Promise.promise()
        mongoClient.find(collection, emptySearchQuery) { res ->
            when {
                res.succeeded() -> {
                    var jobjRes = JsonObject(res.result().get(0).toString())
                    jobjRes.remove("_id")
                    var result = jobjRes.mapTo(TraumaInfo::class.java)
                    promise.complete(result)
                }
                else -> {
                    println(res.cause())
                    promise.fail(res.cause())
                }
            }
        }
        return promise.future()
    }

    fun activateTraumaTeam(teamInfo: TraumaTeamInfo): Future<String> {
        return this.updateField("traumaTeam", JsonObject.mapFrom(teamInfo))
    }

    fun getActivationInfo(): Future<TraumaTeamInfo> {
        return executeDistinctQuery("traumaTeam", TraumaTeamInfo::class.java)
    }

    fun takePatientInCharge(psCode: Int): Future<String> {
        return this.updateField("psCode", psCode)
    }

    companion object {
        fun createTrauma(mongoConfigPath: String): Promise<TraumaInfoService> {
            val traumaInfoPromise = Promise.promise<TraumaInfoService>()
            TraumaInfoService(mongoConfigPath, traumaInfoPromise)
            return traumaInfoPromise
        }
    }
}

class TraumaInfo(
    @JsonProperty("traumaTeam") val traumaTeam: TraumaTeamInfo = TraumaTeamInfo(),
    @JsonProperty("psCode") val psCode: Int = 0,
    @JsonProperty("finalDestination") val finalDestination: String = ""
)

class TraumaTeamInfo(
    @JsonProperty("traumaLeaderName") val traumaLeaderName: String = "",
    @JsonProperty("activation") val activation: String = ""
)