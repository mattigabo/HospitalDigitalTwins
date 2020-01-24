package digitaltwinframework

import digitaltwinframework.coreimplementation.RESTServer

interface DigitalTwinSystem {

    val name: String

    fun RESTServerInstance(): RESTServer

    fun shutdown()
}
