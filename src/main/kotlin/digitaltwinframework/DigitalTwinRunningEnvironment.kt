package digitaltwinframework

import java.net.URI

/**
 * The class that implements this interface will represents the execution environment
 * where the digital twin is running (e.g. the process that execute the digital twin)
 * */
interface DigitalTwinRunningEnvironment {
    val name: String
    fun executeDigitalTwin(id: URI, factory: DigitalTwinFactory)
    fun shutdown()
}