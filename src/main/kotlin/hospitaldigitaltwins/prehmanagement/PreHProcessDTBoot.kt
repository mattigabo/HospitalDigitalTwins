package hospitaldigitaltwins.prehmanagement

import digitaltwinframework.coreimplementation.BasicRunningEnvironment
import java.net.URI

fun main() {
    val testIdentifier = "testEnvironment"

    BasicRunningEnvironment.boot("PreHDTRunningEnv").onComplete {
        it.result().executeDigitalTwin(URI("PreHID"), PreHProcessDTFactory())
    }.onFailure { it.printStackTrace() }
}