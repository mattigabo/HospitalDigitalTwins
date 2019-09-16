package digitaltwinframework

interface DigitalTwinManager {
    fun createDigitalTwin()
    fun enstablishLink(firstDT: DigitalTwinMetaInfo, secondDT: DigitalTwinMetaInfo, semantic: LinkSemantic)
    fun deleteLingk(link: DigitalTwinLink)
}

interface DigitalTwinLink {
    val firstDigitalTwin: DigitalTwinMetaInfo
    val secondDigitalTwin: DigitalTwinMetaInfo
    val semantic: LinkSemantic
}
