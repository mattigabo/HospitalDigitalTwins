package digitaltwinframework.coreimplementation


import com.fasterxml.jackson.annotation.JsonProperty
import digitaltwinframework.coreimplementation.restmanagement.CoreManagementApiRESTAdapter
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

open class CoreManagementEvolutionController(val thisDT: AbstractDigitalTwin) : AbstractVerticle() {

    private lateinit var coreManagAdapter: CoreManagementApiRESTAdapter

    val relationService = RelationService()

    override fun start() {
        super.start()
        coreManagAdapter = CoreManagementApiRESTAdapter(thisDT.runningEnv.vertx, thisDT.identifier.toString())
        this.registerCoreHandlerToEventBus(thisDT.runningEnv.eventBus)
        coreManagAdapter.loadOpenApiSpec()
    }

    private fun registerCoreHandlerToEventBus(eb: EventBus) {

        eb.consumer<Any>(coreManagAdapter.GET_ID_BUS_ADDR) { message ->
            message.reply(
                """
                   {
                        "digitalTwinIdentifier": ${thisDT.identifier}
                   }
               """.trimIndent()
            )
        }

        eb.consumer<JsonObject>(coreManagAdapter.ADD_LINK_TO_ANOTHER_DT_BUS_ADDR) { message ->
            var link = CoreManagementSchemas.LinkToAnotherDigitalTwin(
                message.body().getString("otherDigitalTwin"),
                message.body().getJsonObject("semantics")
            )

            relationService.addRelation(link.otherDigitalTwin, link.semantics)
            message.reply(StandardMessages.OPERATION_EXECUTED_MESSAGE)
        }

        eb.consumer<JsonArray>(coreManagAdapter.GET_ALL_LINK_TO_OTHER_DT_BUS_ADDR) { message ->
            val links = relationService.relationToOtherDT
                .flatMap { entry ->
                    entry.value.map {
                        CoreManagementSchemas.LinkToAnotherDigitalTwin(entry.key.toString(), it)
                    }
                }
            val reply = JsonArray(links.map { JsonObject.mapFrom(it) })

            message.reply(reply)
        }

        eb.consumer<JsonObject>(coreManagAdapter.DELETE_LINK_BUS_ADDR) { message ->
            val linkToDT = CoreManagementSchemas.LinkToAnotherDigitalTwin(
                message.body().getString("otherDigitalTwin"),
                message.body().getJsonObject("semantics")
            )
            var wasDeleted = false
            relationService.relationToOtherDT.get(linkToDT.otherDigitalTwin)?.let {
                if (it.contains(linkToDT.semantics)) {
                    wasDeleted = relationService.deleteRelation(linkToDT.otherDigitalTwin, linkToDT.semantics)
                }
            }

            message.reply(wasDeleted)
        }

        eb.consumer<Any>(coreManagAdapter.SHUTDOWN_DT_BUS_ADDR) { message ->
            message.reply("Started the shutdown procedure...")
            thisDT.shutdown()
        }
    }


    override fun stop() {
        super.stop()
        println("Evolution Controller Termination")
    }
}


object CoreManagementSchemas {
    data class LinkToAnotherDigitalTwin(
        @JsonProperty("otherDigitalTwin") val otherDigitalTwin: String,
        @JsonProperty("semantics") val semantics: JsonObject
    )
}