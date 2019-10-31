package digitaltwinframework


interface DigitalTwinLink {
    val firstDigitalTwin: DigitalTwinMetaInfo
    val secondDigitalTwin: DigitalTwinMetaInfo
    val semantic: LinkSemantic
}