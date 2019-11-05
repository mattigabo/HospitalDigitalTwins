package digitaltwinframework.coreimplementation

import digitaltwinframework.DigitalTwin
import digitaltwinframework.EvolutionController
import digitaltwinframework.LinkSemantic
import digitaltwinframework.coreimplementation.eventbusutils.StandardMessages.OPERATION_EXECUTED_MESSAGE
import digitaltwinframework.coreimplementation.eventbusutils.SystemEventBusAddresses
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonArray
import java.net.URI

/**
 * This class constitutes an abstract implementation of a Digital Twin
 * that provide basic implementation of the management of the core aspects
 *
 * */
abstract class AbstractDigitalTwin(
        override val identifier: URI,
        override val executionEngine: BasicDigitalTwinExecutionEngine
) : DigitalTwin {

    var relationToOtherDT: MutableMap<URI, ArrayList<LinkSemantic>> = HashMap()

    override val evolutionController: BasicEvolutionController = BasicEvolutionController(this)
    val EVOLUTION_CONTROLLER_ADDRESS = SystemEventBusAddresses.EVOLUTION_CONTROLLER_SUFFIX.preappend(identifier.toString())

    override fun addLink(digitalTwinId: URI, semantic: LinkSemantic) {
        if (this.relationToOtherDT.containsKey(digitalTwinId)) {
            this.relationToOtherDT.get(digitalTwinId)?.add(semantic)
        } else {
            var linkList = ArrayList<LinkSemantic>()
            linkList.add(semantic)
            this.relationToOtherDT.put(digitalTwinId, linkList)
        }
    }

    override fun deleteLink(digitalTwinId: URI, semantic: LinkSemantic): Boolean {
        return this.relationToOtherDT.get(digitalTwinId)?.remove(semantic) ?: false
    }
}

class BasicEvolutionController(val thisDT: AbstractDigitalTwin) : EvolutionController, AbstractVerticle() {

    var coreManagAdapter = CoreManagementApiRESTAdapter(thisDT)

    override fun start() {
        super.start()
        this.registerCoreHandlerToEventBus(thisDT.executionEngine.eventBus)
    }

    private fun registerCoreHandlerToEventBus(eb: EventBus) {

        eb.consumer<Any>(coreManagAdapter.GET_IDENTIFIER_BUS_ADDR) { message ->
            message.reply("""
                   {
                        "digitalTwinIdentifier":{${thisDT.identifier}}
                   }
               """.trimIndent())
        }

        eb.consumer<Any>(coreManagAdapter.ADD_LINK_TO_ANOTHER_DIGITAL_TWIN_BUS_ADDR) { message ->
            when (message.body()) {
                is CoreManagementSchemas.LinkToAnotherDigitalTwin -> {
                    val link = message.body() as CoreManagementSchemas.LinkToAnotherDigitalTwin
                    thisDT.addLink(URI(link.otherDigitalTwin), link.semantic)
                    message.reply(OPERATION_EXECUTED_MESSAGE)
                }
            }
        }

        eb.consumer<Any>(coreManagAdapter.GET_ALL_LINK_TO_OTHER_DIGITAL_TWINS_BUS_ADDR) { message ->
            val reply = JsonArray(thisDT.relationToOtherDT.map { entry ->
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
                    thisDT.relationToOtherDT.get(URI(link.otherDigitalTwin))?.let {
                        if (it.contains(link.semantic)) {
                            wasDeleted = thisDT.deleteLink(URI(link.otherDigitalTwin), link.semantic)
                        }
                    }
                }
            }
            message.reply(wasDeleted)
        }

        eb.consumer<Any>(coreManagAdapter.SHUTDOWN_DIGITAL_TWIN_BUS_ADDR) { message ->
            message.reply("Started the shutdown procedure...")
            this.stop()
            System.exit(1)
        }
    }


    override fun stop() {
        super.stop()
        println("Evolution Manager Termination")
    }
}
