package digitaltwinframework

import java.net.URI

interface DigitalTwinManager {
    fun createDigitalTwin(factory: DigitalTwinFactory): URI
    fun killDigitalTwin(target: URI)
    fun enstablishLink(firstDT: DigitalTwinMetaInfo, secondDT: DigitalTwinMetaInfo, semantic: LinkSemantic)
    fun deleteLingk(link: DigitalTwinLink)
}


interface DigitalTwinLink {
    val firstDigitalTwin: DigitalTwinMetaInfo
    val secondDigitalTwin: DigitalTwinMetaInfo
    val semantic: LinkSemantic
}

interface IdentifierGenerator {
    fun nextIdentifier(): URI
}