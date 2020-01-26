package hospitaldigitaltwins.prehmanagement

import digitaltwinframework.coreimplementation.BasicDigitalTwinRunningEnvironment
import java.net.URI

fun main() {
    val testIdentifier = "testEnvironment"
    val runningEnv = BasicDigitalTwinRunningEnvironment("PreHDTRunningEnv")
    runningEnv.executeDigitalTwin(URI("PreHID"), PreHProcessDTFactory())
}