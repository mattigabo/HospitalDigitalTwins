package digitaltwinframework.coreimplementation

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
class CoreManagementApiRESTAdapter(thisDT: AbstractDigitalTwin) : AbstractRESTInteractionAdapter(thisDT) {

    override val adapterName: String = "DigitalTwinCoreManagementRESTApi"
    private val apiSpecsPath = ConfigUtils.createUri("/framework/DigitalTwinManagementApi-0.1-OpenApi-Schemas.yaml")

    val GET_ID_BUS_ADDR = composeAddress(thisDT.EVOLUTION_CONTROLLER_ADDRESS, OperationIDS.GET_IDENTIFIER)
    val ADD_LINK_TO_ANOTHER_DT_BUS_ADDR = composeAddress(thisDT.EVOLUTION_CONTROLLER_ADDRESS, OperationIDS.ADD_LINK_TO_ANOTHER_DIGITAL_TWIN)
    val GET_ALL_LINK_TO_OTHER_DT_BUS_ADDR = composeAddress(thisDT.EVOLUTION_CONTROLLER_ADDRESS, OperationIDS.GET_ALL_LINK_TO_OTHER_DIGITAL_TWINS)
    val DELETE_LINK_BUS_ADDR = composeAddress(thisDT.EVOLUTION_CONTROLLER_ADDRESS, OperationIDS.DELETE_LINK)
    val SHUTDOWN_DT_BUS_ADDR = composeAddress(thisDT.EVOLUTION_CONTROLLER_ADDRESS, OperationIDS.SHUTDOWN_DIGITAL_TWIN)


    val onIdRequestHandler = Handler<RoutingContext> { routingContext ->
        thisDT.runningEnvironment.eventBus.request<String>(GET_ID_BUS_ADDR, "") { ar ->
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

        thisDT.runningEnvironment.eventBus.request<String>(ADD_LINK_TO_ANOTHER_DT_BUS_ADDR, linkToDT) { ar ->
            if (ar.succeeded()) {
                sendSuccessResponse(ar.result().body(), routingContext)
            } else if (ar.failed()) {
                sendServerErrorResponse(routingContext)
            }
        }
    }

    val onGetAllLinksToOtherDTHandler = Handler<RoutingContext> { routingContext ->
        thisDT.runningEnvironment.eventBus.request<String>(GET_ALL_LINK_TO_OTHER_DT_BUS_ADDR, EMPTY_MESSAGE) { ar ->
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
            textualSemantics(requestContentJson.getString("semantic"))
        )

        thisDT.runningEnvironment.eventBus.request<String>(DELETE_LINK_BUS_ADDR, linkToDT) { ar ->
            if (ar.succeeded()) {
                sendSuccessResponse(ar.result().body(), routingContext)
            } else if (ar.failed()) {
                sendNotFoundResponse("link not found", routingContext)
            }
        }
    }

    val onShutdownDTHandler = Handler<RoutingContext> { routingContext ->
        thisDT.runningEnvironment.eventBus.request<String>(SHUTDOWN_DT_BUS_ADDR, EMPTY_MESSAGE) { ar ->
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
                OperationIDS.GET_IDENTIFIER to onIdRequestHandler,
                OperationIDS.ADD_LINK_TO_ANOTHER_DIGITAL_TWIN to addLinkToAnotherDTHandler,
                OperationIDS.GET_ALL_LINK_TO_OTHER_DIGITAL_TWINS to onGetAllLinksToOtherDTHandler,
                OperationIDS.DELETE_LINK to onDeleteLinkHandler,
                OperationIDS.SHUTDOWN_DIGITAL_TWIN to onShutdownDTHandler
        )
    }

    companion object {
        object OperationIDS {
            const val GET_IDENTIFIER = "getIdentifier"
            const val ADD_LINK_TO_ANOTHER_DIGITAL_TWIN = "addLinkToAnotherDigitalTwin"
            const val GET_ALL_LINK_TO_OTHER_DIGITAL_TWINS = "getAllLinkToOtherDigitalTwins"
            const val DELETE_LINK = "deleteLink"
            const val SHUTDOWN_DIGITAL_TWIN = "shutdownDigitalTwin"
        }
    }
}


object CoreManagementSchemas {
    data class LinkToAnotherDigitalTwin(val otherDigitalTwin: String, val semantic: Semantics)
}