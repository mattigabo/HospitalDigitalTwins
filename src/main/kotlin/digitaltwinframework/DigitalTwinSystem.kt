package digitaltwinframework

import digitaltwinframework.coreimplementation.restmanagement.RESTServer

interface DigitalTwinSystem {

    val name: String

    fun RESTServerInstance(): RESTServer

    fun shutdown()
}
