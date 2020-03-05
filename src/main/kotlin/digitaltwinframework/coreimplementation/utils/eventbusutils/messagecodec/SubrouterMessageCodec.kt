package digitaltwinframework.coreimplementation.utils.eventbusutils.messagecodec

import digitaltwinframework.coreimplementation.restmanagement.RestServer
import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router

/**
 * Created by Matteo Gabellini on 30/09/2019.
 */
class SubrouterMessageCodec : MessageCodec<RestServer.RegisterSubrouter, RestServer.RegisterSubrouter> {

    override fun systemCodecID(): Byte {
        return -1
    }

    override fun encodeToWire(buffer: Buffer, s: RestServer.RegisterSubrouter) {
        val jsonToEncode = JsonObject()
        jsonToEncode.put("handlerServiceId", s.handlerServiceId)
        jsonToEncode.put("router", s.router)

        // Encode object to string
        val jsonToStr = jsonToEncode.encode()

        // Length of JSON: is NOT characters count
        val length = jsonToStr.toByteArray().size

        // Write data into given buffer
        buffer.appendInt(length)
        buffer.appendString(jsonToStr)
    }

    override fun decodeFromWire(pos: Int, buffer: Buffer): RestServer.RegisterSubrouter {
        // Length of JSON
        val length = buffer.getInt(pos)

        // Get JSON string by it`s length
        // Jump 4 because getInt() == 4 bytes
        val jsonStr = buffer.getString(pos + 4, pos + 4 + length)
        val contentJson = JsonObject(jsonStr)

        val handlerServiceId: String = contentJson.getString("handlerServiceId")
        val router: Router = contentJson.getValue("unit") as Router

        return RestServer.RegisterSubrouter(handlerServiceId, router)
    }

    override fun transform(s: RestServer.RegisterSubrouter): RestServer.RegisterSubrouter {
        return s
    }

    override fun name(): String {
        return this.javaClass.simpleName
    }
}