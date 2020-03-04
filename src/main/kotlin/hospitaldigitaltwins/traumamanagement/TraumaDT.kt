package hospitaldigitaltwins.traumamanagement

import digitaltwinframework.DigitalTwin
import digitaltwinframework.DigitalTwinFactory
import digitaltwinframework.coreimplementation.AbstractDigitalTwin
import java.net.URI

class TraumaDT(override val identifier: URI) : AbstractDigitalTwin(identifier)

class TraumaDTFactory : DigitalTwinFactory {
    override fun create(id: URI): DigitalTwin = TraumaDT(id)
}