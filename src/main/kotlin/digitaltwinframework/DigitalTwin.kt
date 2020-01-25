package digitaltwinframework

import java.net.URI
import java.time.Instant

interface DigitalTwin : Identifiable<URI> {
    val metaInfo: MetaInfo
    val dataModel: Model

    /**
     * Stopping the digital twin causes the termination of the evolution of the digital twin and the resource release
     * */
    fun shutdown()
}


interface MetaInfo


open class DigitalTwinValue<T>(open val value: T, open val generationTime: Instant)

/**
 * Classes that implements this interface model physical counterpart information,
 * that the digital twin must store and manage such as: Physiological, structural, behavioural information.
 * */
interface Model

interface InteractionAdapter {
    val adapterName: String
}

interface PhysicalWorldInteractionAdapter : InteractionAdapter
interface DigitalTwinInteractionAdapter : InteractionAdapter

interface SourceOfInformation

interface DigitalTwinFactory {
    fun create(id: URI): DigitalTwin
}