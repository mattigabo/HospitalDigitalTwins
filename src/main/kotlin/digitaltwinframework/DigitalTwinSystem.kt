package digitaltwinframework

import digitaltwinframework.coreimplementation.restmanagement.RestServer

interface DigitalTwinSystem {

    val name: String

    fun RESTServerInstance(): RestServer

    fun shutdown()
}
