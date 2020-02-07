package hospitaldigitaltwins.prehmanagement

import digitaltwinframework.coreimplementation.BasicRunningEnvironment
import java.net.URI

fun main() {
    val testIdentifier = "testEnvironment"

    BasicRunningEnvironment.boot("PreHDTRunningEnv") {
        it.executeDigitalTwin(URI("PreHID"), PreHProcessDTFactory())
    }
}