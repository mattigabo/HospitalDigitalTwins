package hospitaldigitaltwins.traumamanagement

import digitaltwinframework.coreimplementation.BasicRunningEnvironment
import hospitaldigitaltwins.prehmanagement.PreHDTFactory
import java.net.URI

/**
 * Created by Matteo Gabellini on 04/03/2020.
 */
fun main(args: Array<String>) {
    val testIdentifier = "testEnvironment"

    BasicRunningEnvironment.boot("TraumaDTRunningEnv").onComplete {
        it.result().executeDigitalTwin(URI("TraumaID"), PreHDTFactory())
    }.onFailure { it.printStackTrace() }
}