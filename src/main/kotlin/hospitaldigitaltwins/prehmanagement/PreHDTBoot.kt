package hospitaldigitaltwins.prehmanagement

import digitaltwinframework.coreimplementation.BasicRunningEnvironment
import java.net.URI

fun main() {
    val testIdentifier = "testPreHEnvironment"

    BasicRunningEnvironment.boot("PreHDTRunningEnv").onComplete {
        it.result().executeDigitalTwin(URI("PreHID"), PreHDTFactory())
    }.onFailure { it.printStackTrace() }
}