package hospitaldigitaltwins.traumamanagement.info

import com.fasterxml.jackson.annotation.JsonProperty
import hospitaldigitaltwins.common.AbstractMongoClientService
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
        eb.consumer<JsonObject>(TraumaTeamOperationIds.ACTIVATE_TRAUMA_TEAM) { message ->

        }

        eb.consumer<JsonObject>(TraumaTeamOperationIds.GET_ACTIVATION_INFO) { message ->

        }

        eb.consumer<JsonObject>(TraumaTeamOperationIds.TAKE_PATIENT_IN_CHARGE) { message ->

        }

        eb.consumer<JsonObject>(FinalDestinationOperationIds.SET_FINAL_DESTINATION) { message ->

        }

        eb.consumer<JsonObject>(FinalDestinationOperationIds.GET_FINAL_DESTINATION) { message ->

        }
    }

    fun activateTraumaTeam(teamInfo: TraumaTeamInfo): Future<String> {
        return this.updateField("traumaTeam", JsonObject.mapFrom(teamInfo))
    }

    fun getActivationInfo(): Future<TraumaTeamInfo> {
        return executeDistinctQuery("traumaTeam", TraumaTeamInfo::class.java)
    }

    fun takePatientInCharge(psCode: Int): Future<String> {
        return this.updateField("psCode", JsonObject.mapFrom(psCode))
    }

    fun setFinalDestination(finalDestination: String): Future<String> {
        return this.updateField("finalDestination", JsonObject.mapFrom(finalDestination))
    }
}

class TraumaInfo(
    @JsonProperty("traumaTeam") traumaTeam: TraumaTeamInfo = TraumaTeamInfo(),
    @JsonProperty("psCode") psCode: Int = 0,
    @JsonProperty("finalDestination") finalDestination: String = ""
)

class TraumaTeamInfo(
    @JsonProperty("traumaLeaderName") traumaLeaderName: String = "",
    @JsonProperty("activation") activation: String = ""
)