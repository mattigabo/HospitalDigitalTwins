package digitaltwinframework

import digitaltwinframework.coreimplementation.Semantics
import java.net.URI


interface DigitalTwinRelation {
    val firstDigitalTwin: URI
    val secondDigitalTwin: URI
    val semantics: Semantics
}