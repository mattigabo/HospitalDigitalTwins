package hospitaldigitaltwins.prehmanagement

import digitaltwinframework.coreimplementation.BasicRunningEnvironment

fun main() {
    val testIdentifier = "testPreHEnvironment"

    BasicRunningEnvironment.boot("PreHDTRunningEnv").onComplete {
        it.result().executeDigitalTwin("PreHID", PreHDTFactory())
    }.onFailure { it.printStackTrace() }
}