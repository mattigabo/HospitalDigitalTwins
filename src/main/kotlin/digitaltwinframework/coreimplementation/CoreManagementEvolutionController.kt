package digitaltwinframework.coreimplementation


import digitaltwinframework.coreimplementation.restmanagement.CoreManagementApiRESTAdapter
import digitaltwinframework.coreimplementation.restmanagement.CoreManagementSchemas
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonArray
import java.net.URI

open class CoreManagementEvolutionController(val thisDT: AbstractDigitalTwin) : AbstractVerticle() {

    private lateinit var coreManagAdapter: CoreManagementApiRESTAdapter

    val relationManager = RelationManager()

    override fun start() {
        super.start()
        coreManagAdapter = CoreManagementApiRESTAdapter(thisDT.runningEnv.vertx, thisDT.identifier.toString())
        this.registerCoreHandlerToEventBus(thisDT.runningEnv.eventBus)
        coreManagAdapter.loadOpenApiSpec()
    }

    private fun registerCoreHandlerToEventBus(eb: EventBus) {

        eb.consumer<Any>(coreManagAdapter.GET_ID_BUS_ADDR) { message ->
            message.reply("""
                   {
                        "digitalTwinIdentifier":{${thisDT.identifier}}
                   }
               """.trimIndent())
        }

        eb.consumer<Any>(coreManagAdapter.ADD_LINK_TO_ANOTHER_DT_BUS_ADDR) { message ->
            when (message.body()) {
                is CoreManagementSchemas.LinkToAnotherDigitalTwin -> {
                    val link = message.body() as CoreManagementSchemas.LinkToAnotherDigitalTwin
                    relationManager.addRelation(URI(link.otherDigitalTwin), link.semantic)
                    message.reply(StandardMessages.OPERATION_EXECUTED_MESSAGE)
                }
            }
        }

        eb.consumer<Any>(coreManagAdapter.GET_ALL_LINK_TO_OTHER_DT_BUS_ADDR) { message ->
            val reply = JsonArray(relationManager.relationToOtherDT.map { entry ->
                entry.value.map {
                    CoreManagementSchemas.LinkToAnotherDigitalTwin(entry.key.toString(), it)
                }
            }.toList())

            message.reply(reply)
        }

        eb.consumer<Any>(coreManagAdapter.DELETE_LINK_BUS_ADDR) { message ->
            var wasDeleted = false
            when (message.body()) {
                is CoreManagementSchemas.LinkToAnotherDigitalTwin -> {
                    val link = message.body() as CoreManagementSchemas.LinkToAnotherDigitalTwin
                    relationManager.relationToOtherDT.get(URI(link.otherDigitalTwin))?.let {
                        if (it.contains(link.semantic)) {
                            wasDeleted = relationManager.deleteRelation(URI(link.otherDigitalTwin), link.semantic)
                        }
                    }
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