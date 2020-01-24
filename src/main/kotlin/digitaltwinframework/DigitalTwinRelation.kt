package digitaltwinframework

import digitaltwinframework.coreimplementation.Semantics


interface DigitalTwinRelation {
    val firstDigitalTwin: MetaInfo
    val secondDigitalTwin: MetaInfo
    val semantics: Semantics
}