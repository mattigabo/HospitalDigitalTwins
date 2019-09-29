package digitaltwinframework

import digitaltwinframework.coreimplementation.RESTServer
import java.net.URI

interface DigitalTwinSystem {
    fun createDigitalTwin(factory: DigitalTwinFactory): URI
    fun killDigitalTwin(target: URI)
    fun enstablishLink(firstDT: DigitalTwinMetaInfo, secondDT: DigitalTwinMetaInfo, semantic: LinkSemantic)
    fun deleteLingk(link: DigitalTwinLink)

    fun RESTServerInstance(): RESTServer

    fun shutdown()
}

interface DigitalTwinLink {
    val firstDigitalTwin: DigitalTwinMetaInfo
    val secondDigitalTwin: DigitalTwinMetaInfo
    val semantic: LinkSemantic
}

interface IdentifierGenerator {
    fun nextIdentifier(): URI
}