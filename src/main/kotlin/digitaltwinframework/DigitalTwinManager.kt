package digitaltwinframework

import java.net.URI

interface DigitalTwinManager {
    fun createDigitalTwin(factory: DigitalTwinFactory): URI
    fun killDigitalTwin(target: URI)
    fun enstablishLink(firstDT: DigitalTwinMetaInfo, secondDT: DigitalTwinMetaInfo, semantic: LinkSemantic)
    fun deleteLink(link: DigitalTwinLink)
}