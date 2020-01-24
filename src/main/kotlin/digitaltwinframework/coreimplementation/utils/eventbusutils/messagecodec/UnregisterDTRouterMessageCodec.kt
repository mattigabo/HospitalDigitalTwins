package digitaltwinframework.coreimplementation.utils.eventbusutils.messagecodec

import digitaltwinframework.coreimplementation.RESTServer
import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import java.net.URI

class UnregisterDTRouterMessageCodec : MessageCodec<RESTServer.UnregisterRouter, RESTServer.UnregisterRouter> {

    override fun systemCodecID(): Byte {
        return -1
    }

    override fun encodeToWire(buffer: Buffer, s: RESTServer.UnregisterRouter) {
        val jsonToEncode = JsonObject()
        jsonToEncode.put("digitalTwinID", s.digitalTwinID)
        jsonToEncode.put("router", s.router)

        // Encode object to string
        val jsonToStr = jsonToEncode.encode()

        // Length of JSON: is NOT characters count
        val length = jsonToStr.toByteArray().size

        // Write data into given buffer
        buffer.appendInt(length)
        buffer.appendString(jsonToStr)
    }

    override fun decodeFromWire(pos: Int, buffer: Buffer): RESTServer.UnregisterRouter {
        // Length of JSON
        val length = buffer.getInt(pos)

        // Get JSON string by it`s length
        // Jump 4 because getInt() == 4 bytes
        val jsonStr = buffer.getString(pos + 4, pos + 4 + length)
        val contentJson = JsonObject(jsonStr)

        val digitalTwinID: URI = URI(contentJson.getString("digitalTwinID"))
        val router: Router = contentJson.getValue("unit") as Router

        return RESTServer.UnregisterRouter(digitalTwinID, router)
    }

    override fun transform(s: RESTServer.UnregisterRouter): RESTServer.UnregisterRouter {
        return s
    }

    override fun name(): String {
        return this.javaClass.simpleName
    }
}