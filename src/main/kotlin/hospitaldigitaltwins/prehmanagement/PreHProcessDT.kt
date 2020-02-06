package hospitaldigitaltwins.prehmanagement

import digitaltwinframework.DigitalTwin
import digitaltwinframework.DigitalTwinFactory
import digitaltwinframework.coreimplementation.AbstractDigitalTwin
import java.net.URI

class PreHProcessDT(identifier: URI) : AbstractDigitalTwin()

class PreHProcessDTFactory : DigitalTwinFactory {
    override fun create(id: URI): DigitalTwin = PreHProcessDT(id)
}