package digitaltwinframework

import java.net.URI

/**
 * The class that implements this interface will represents the execution environment
 * where the digital twin is running (e.g. the process that execute the digital twin)
 * */
interface DigitalTwinRunningEnvironment {
    fun executeDigitalTwin(id: URI, factory: DigitalTwinFactory)
}