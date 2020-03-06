package hospitaldigitaltwins.traumamanagement

import digitaltwinframework.coreimplementation.BasicRunningEnvironment
import digitaltwinframework.coreimplementation.restmanagement.RESTServerConfig

/**
 * Created by Matteo Gabellini on 04/03/2020.
 */
fun main(args: Array<String>) {
    val testIdentifier = "testTraumaEnvironment"
    var traumaRestConfig = RESTServerConfig("localhost", 8081)
    BasicRunningEnvironment.boot("TraumaDTRunningEnv", traumaRestConfig).onComplete {
        it.result().executeDigitalTwin("TraumaID", TraumaDTFactory())
    }.onFailure { it.printStackTrace() }
}