package digitaltwinframework.coreimplementation.restmanagement

import digitaltwinframework.coreimplementation.BasicRunningEnvironment
import digitaltwinframework.coreimplementation.utils.ConfigUtils
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages.EMPTY_MESSAGE
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext

/**
 * This class represents the interaction adapter between the digital twin passed as parameter and the REST Server.
 * The handler defined in this class will be used by the REST server control flow in order to forward the received
 * request about the core management function, to the specified Digital Twin
 * */
class CoreManagementApiRESTAdapter(vertxInstance: Vertx, handlerServiceId: String) :
    AbstractRestInteractionAdapter(vertxInstance, handlerServiceId) {

    override val adapterName: String = "DigitalTwinCoreManagementRESTApi"
    override val openApiSpecPath: String
        get() = ConfigUtils.createUri("/framework/DigitalTwinManagementApi-0.1-OpenApi-Schemas.yaml")


    val eventBus = BasicRunningEnvironment.runningInstance!!.eventBus

    val GET_ID_BUS_ADDR = OperationIDS.GET_ID//composeAddress(handlerServiceId, OperationIDS.GET_ID)
    val ADD_LINK_TO_ANOTHER_DT_BUS_ADDR =
        OperationIDS.ADD_LINK_TO_ANOTHER_DT//composeAddress(handlerServiceId, OperationIDS.ADD_LINK_TO_ANOTHER_DT)
    val GET_ALL_LINK_TO_OTHER_DT_BUS_ADDR =
        OperationIDS.GET_ALL_LINK_TO_OTHER_DT//composeAddress(handlerServiceId, OperationIDS.GET_ALL_LINK_TO_OTHER_DT)
    val DELETE_LINK_BUS_ADDR = OperationIDS.DELETE_LINK//composeAddress(handlerServiceId, OperationIDS.DELETE_LINK)
    val SHUTDOWN_DT_BUS_ADDR = OperationIDS.SHUTDOWN_DT//composeAddress(handlerServiceId, OperationIDS.SHUTDOWN_DT)


    override fun operationCallbackMapping(): Map<String, Handler<RoutingContext>> {
        return mapOf(
            OperationIDS.GET_ID to CoreRestHandlers.onIdRequestHandler(GET_ID_BUS_ADDR),
            OperationIDS.ADD_LINK_TO_ANOTHER_DT to CoreRestHandlers.addLinkToAnotherDTHandler(
                ADD_LINK_TO_ANOTHER_DT_BUS_ADDR
            ),
            OperationIDS.GET_ALL_LINK_TO_OTHER_DT to CoreRestHandlers.onGetAllLinksToOtherDTHandler(
                GET_ALL_LINK_TO_OTHER_DT_BUS_ADDR
            ),
            OperationIDS.DELETE_LINK to CoreRestHandlers.onDeleteLinkHandler(DELETE_LINK_BUS_ADDR),
            OperationIDS.SHUTDOWN_DT to CoreRestHandlers.onShutdownDTHandler(SHUTDOWN_DT_BUS_ADDR)
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

object CoreRestHandlers {
    val onIdRequestHandler: (String) -> Handler<RoutingContext> = { busAddr ->
        Handler<RoutingContext> { routingContext ->
            EventBusRestRequestForwarder.performRequest<String>(routingContext, busAddr, EMPTY_MESSAGE)
        }
    }

    val addLinkToAnotherDTHandler: (String) -> Handler<RoutingContext> = { busAddr ->
        Handler<RoutingContext> { routingContext ->
            val linkToDT = routingContext.bodyAsJson
            EventBusRestRequestForwarder.performRequest<JsonObject>(routingContext, busAddr, linkToDT)
        }
    }

    val onGetAllLinksToOtherDTHandler: (String) -> Handler<RoutingContext> = { busAddr ->
        Handler<RoutingContext> { routingContext ->
            EventBusRestRequestForwarder.performRequest<JsonArray>(routingContext, busAddr, EMPTY_MESSAGE)
        }
    }

    val onDeleteLinkHandler: (String) -> Handler<RoutingContext> = { busAddr ->
        Handler<RoutingContext> { routingContext ->
            val requestContentJson = routingContext.bodyAsJson
            EventBusRestRequestForwarder.performRequest<JsonObject>(routingContext, busAddr, requestContentJson)
        }
    }

    val onShutdownDTHandler: (String) -> Handler<RoutingContext> = { busAddr ->
        Handler<RoutingContext> { routingContext ->
            EventBusRestRequestForwarder.performRequest<String>(routingContext, busAddr, EMPTY_MESSAGE)
        }
    }
}