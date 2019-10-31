package digitaltwinframework.coreimplementation

import digitaltwinframework.InteractionAdapter
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory

class BasicManagementApiRESTAdapter : InteractionAdapter {
    private val apiSpecsPath = ConfigUtils.createUri("/framework/DigitalTwinManagementApi-0.1-OpenApi-Schemas.yaml")

    fun onIdentifierRequest() {

    }

    fun addLinkToAnotherDigitalTwin() {

    }

    fun onGetAllLinksToOtherDigitalTwin() {

    }

    fun onDeleteLink() {

    }

    fun onShutdownDigitalTwin{

    }

    fun loadOpenApiSpec() {
        OpenAPI3RouterFactory.create(thisDT.dtSystem.vertx, thisDT.metaInfo.openApiSpecificationPath) { asyncResult ->
            if (asyncResult.succeeded()) {
                var routerFactory: OpenAPI3RouterFactory = asyncResult.result()

                routerFactory.addHandlerByOperationId(OperationIDS.TEMPERATURE_MEASURED, temperatureRequestHandler)

                this.registerToTheRunningServer(routerFactory.router)
            } else {
                println("ERROR")
                // Something went wrong during router factory initialization
                val exception = asyncResult.cause()
                println(exception)
            }
        }
    }
}

object OperationIDS {
    const val GET_IDENTIFIER = "getIdentifier"
    const val ADD_LINK_TO_ANOTHER_DIGITAL_TWIN = "addLinkToAnotherDigitalTwin"
    const val GET_ALL_LINK_TO_OTHER_DIGITAL_TWINS = "getAllLinkToOtherDigitalTwins"
    const val DELETE_LINK = "deleteLink"
    const val SHUTDOWN_DIGITAL_TWIN = "shutdownDigitalTwin"
}