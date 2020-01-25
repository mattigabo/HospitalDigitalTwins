package digitaltwinframework

import digitaltwinframework.coreimplementation.Identifiable
import digitaltwinframework.coreimplementation.Semantics
import java.net.URI
import java.time.Instant

interface DigitalTwin : Identifiable<URI> {
    val metaInfo: MetaInfo
    val dataModel: Model
    val evolutionController: EvolutionController
    val runningEnvironment: DigitalTwinRunningEnvironment

    /**
     * Add a semantic link to another digital twin
     * */
    fun addLink(digitalTwinId: URI, semantic: Semantics)

    /**
     * Delete a semantic link to another digital twin if present
     * */
    fun deleteLink(digitalTwinId: URI, semantic: Semantics): Boolean

    /**
     * Stopping the digital twin causes the termination of the evolution of the digital twin and the resource release
     * */
    fun stop()
}


interface MetaInfo


open class DigitalTwinValue<T>(open val value: T, open val generationTime: Instant)

/**
 * Classes that implements this interface model physical counterpart information,
 * that the digital twin must store and manage such as: Physiological, structural, behavioural information.
 * */
interface Model

/*
* This component encapsulate the interaction with the physical counter part in order to maintain the physical model updated,
* It also should react and update the physical model if the some interaction with other digital twin could reflect an interaction
* in the physical world that could have consequences on the physical counterpart
* */
interface EvolutionController {
    fun start()
    fun stop()
}


interface InteractionAdapter {
    val adapterName: String
}

interface PhysicalWorldInteractionAdapter : InteractionAdapter
interface DigitalTwinInteractionAdapter : InteractionAdapter

interface DigitalTwinFactory {
    fun create(id: URI): DigitalTwin
}