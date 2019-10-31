package digitaltwinframework

import digitaltwinframework.coreimplementation.RESTServer
import java.net.URI

interface DigitalTwinSystem {

    val name: String

    fun RESTServerInstance(): RESTServer

    fun shutdown()
}

interface IdentifierGenerator {
    fun nextIdentifier(): URI
}