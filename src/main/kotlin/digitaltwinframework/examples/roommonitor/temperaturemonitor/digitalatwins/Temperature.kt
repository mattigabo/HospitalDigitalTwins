package digitaltwinframework.examples.roommonitor.temperaturemonitor.digitalatwins

import digitaltwinframework.DigitalTwinValue
import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import io.vertx.core.json.JsonObject
import java.time.Instant


data class Temperature(override val value: Int, val unit: String, override val generationTime: Instant) : DigitalTwinValue<Int>(value, generationTime)

class TemperatureMessageCodec : MessageCodec<Temperature, Temperature> {
    override fun systemCodecID(): Byte {
        return -1
    }

    override fun encodeToWire(buffer: Buffer, s: Temperature) {
        val jsonToEncode = JsonObject()
        jsonToEncode.put("value", s.value)
        jsonToEncode.put("unit", s.unit)
        jsonToEncode.put("generationTime", s.generationTime)

        // Encode object to string
        val jsonToStr = jsonToEncode.encode()

        // Length of JSON: is NOT characters count
        val length = jsonToStr.toByteArray().size

        // Write data into given buffer
        buffer.appendInt(length)
        buffer.appendString(jsonToStr)
    }

    override fun decodeFromWire(pos: Int, buffer: Buffer): Temperature {
        // Length of JSON
        val length = buffer.getInt(pos)

        // Get JSON string by it`s length
        // Jump 4 because getInt() == 4 bytes
        val jsonStr = buffer.getString(pos + 4, pos + 4 + length)
        val contentJson = JsonObject(jsonStr)

        val value: Int = contentJson.getInteger("value")
        val unit: String = contentJson.getString("unit")
        val generationTime: Instant = contentJson.getInstant("generationTime")

        return Temperature(value, unit, generationTime)
    }

    override fun transform(s: Temperature): Temperature {
        return s
    }

    override fun name(): String {
        return this.javaClass.simpleName
    }
}