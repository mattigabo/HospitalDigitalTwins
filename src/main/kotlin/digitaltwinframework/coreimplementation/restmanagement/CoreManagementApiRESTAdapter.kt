package digitaltwinframework.coreimplementation.restmanagement

import digitaltwinframework.coreimplementation.AbstractDigitalTwin
import digitaltwinframework.coreimplementation.BasicDigitalTwinRunningEnvironment
import digitaltwinframework.coreimplementation.Semantics
import digitaltwinframework.coreimplementation.textualSemantics
import digitaltwinframework.coreimplementation.utils.ConfigUtils
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages.EMPTY_MESSAGE
import digitaltwinframework.coreimplementation.utils.eventbusutils.SystemEventBusAddresses.Companion.composeAddress
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

/**
 * This class represents the interaction adapter between the digital twin passed as parameter and the REST Server.
 * The handler defined in this class will be used by the REST server control flow in order to forward the received
 * request about the core management function, to the specified Digital Twin
 * */
class CoreManagementApiRESTAdapter(thisDT: AbstractDigitalTwin)
    : AbstractRESTInteractionAdapter(thisDT.evolutionController.vertx, thisDT) {

    override val adapterName: String = "DigitalTwinCoreManagementRESTApi"
    private val apiSpecsPath = ConfigUtils.createUri("/framework/DigitalTwinManagementApi-0.1-OpenApi-Schemas.yaml")

    val eventBus = BasicDigitalTwinRunningEnvironment.runningInstance!!.eventBus

    val GET_ID_BUS_ADDR = composeAddress(thisDT.EVOLUTION_CONTROLLER_ADDRESS, OperationIDS.GET_ID)
    val ADD_LINK_TO_ANOTHER_DT_BUS_ADDR = composeAddress(thisDT.EVOLUTION_CONTROLLER_ADDRESS, OperationIDS.ADD_LINK_TO_ANOTHER_DT)
    val GET_ALL_LINK_TO_OTHER_DT_BUS_ADDR = composeAddress(thisDT.EVOLUTION_CONTROLLER_ADDRESS, OperationIDS.GET_ALL_LINK_TO_OTHER_DT)
    val DELETE_LINK_BUS_ADDR = composeAddress(thisDT.EVOLUTION_CONTROLLER_ADDRESS, OperationIDS.DELETE_LINK)
    val SHUTDOWN_DT_BUS_ADDR = composeAddress(thisDT.EVOLUTION_CONTROLLER_ADDRESS, OperationIDS.SHUTDOWN_DT)


    val onIdRequestHandler = Handler<RoutingContext> { routingContext ->
        eventBus.request<String>(GET_ID_BUS_ADDR, "") { ar ->
            if (ar.succeeded()) {
                sendSuccessResponse(ar.result().body(), routingContext)
            } else if (ar.failed()) {
                sendServerErrorResponse(routingContext)
            }
        }
    }

    val addLinkToAnotherDTHandler = Handler<RoutingContext> { routingContext ->
        val requestContentJson = routingContext.bodyAsJson
        val linkToDT = CoreManagementSchemas.LinkToAnotherDigitalTwin(
                requestContentJson.getString("otherDigitalTwin"),
                textualSemantics(requestContentJson.getString("semantic"))
        )

        eventBus.request<String>(ADD_LINK_TO_ANOTHER_DT_BUS_ADDR, linkToDT) { ar ->
            if (ar.succeeded()) {
                sendSuccessResponse(ar.result().body(), routingContext)
            } else if (ar.failed()) {
                sendServerErrorResponse(routingContext)
            }
        }
    }

    val onGetAllLinksToOtherDTHandler = Handler<RoutingContext> { routingContext ->
        eventBus.request<String>(GET_ALL_LINK_TO_OTHER_DT_BUS_ADDR, EMPTY_MESSAGE) { ar ->
            if (ar.succeeded()) {
                sendSuccessResponse(ar.result().body(), routingContext)
            } else if (ar.failed()) {
                sendServerErrorResponse(routingContext)
            }
        }
    }

    val onDeleteLinkHandler = Handler<RoutingContext> { routingContext ->
        val requestContentJson = routingContext.bodyAsJson
        val linkToDT = CoreManagementSchemas.LinkToAnotherDigitalTwin(
                requestContentJson.getString("otherDigitalTwin"),
                textualSemantics(requestContentJson.getString("semantic")
                )
        )

        eventBus.request<String>(DELETE_LINK_BUS_ADDR, linkToDT) { ar ->
            if (ar.succeeded()) {
                sendSuccessResponse(ar.result().body(), routingContext)
            } else if (ar.failed()) {
                sendNotFoundResponse("link not found", routingContext)
            }
        }
    }

    val onShutdownDTHandler = Handler<RoutingContext> { routingContext ->
        eventBus.request<String>(SHUTDOWN_DT_BUS_ADDR, EMPTY_MESSAGE) { ar ->
            if (ar.succeeded()) {
                sendSuccessResponse(ar.result().body(), routingContext)
            }
        }
    }

    override fun getOpenApiSpec(): String {
        return apiSpecsPath
    }

    override fun operationCallbackMapping(): Map<String, Handler<RoutingContext>> {
        return mapOf(
                OperationIDS.GET_ID to onIdRequestHandler,
                OperationIDS.ADD_LINK_TO_ANOTHER_DT to addLinkToAnotherDTHandler,
                OperationIDS.GET_ALL_LINK_TO_OTHER_DT to onGetAllLinksToOtherDTHandler,
                OperationIDS.DELETE_LINK to onDeleteLinkHandler,
                OperationIDS.SHUTDOWN_DT to onShutdownDTHandler
        )
    }

    companion object {
        object OperationIDS {
            const val GET_ID = "getIdentifier"
            const val ADD_LINK_TO_ANOTHER_DT = "addLinkToAnotherDigitalTwin"
            const val GET_ALL_LINK_TO_OTHER_DT = "getAllLinkToOtherDigitalTwins"
            const val DELETE_LINK = "deleteLink"
            const val SHUTDOWN_DT = "shutdownDigitalTwin"
        }
    }
}


object CoreManagementSchemas {
    data class LinkToAnotherDigitalTwin(val otherDigitalTwin: String, val semantic: Semantics)
}