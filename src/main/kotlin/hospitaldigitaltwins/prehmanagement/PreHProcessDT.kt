package hospitaldigitaltwins.prehmanagement

import digitaltwinframework.DigitalTwin
import digitaltwinframework.DigitalTwinFactory
import io.vertx.core.AbstractVerticle
import java.net.URI

class PreHProcessDT(identifier: URI) : AbstractVerticle() {

}

class PreHProcessDTFactory : DigitalTwinFactory {
    override fun create(id: URI): DigitalTwin = PreHProcessDT(id)
}