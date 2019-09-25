package digitaltwinframework

import java.net.URI
import java.time.Instant

interface DigitalTwin {
    val identifier: URI
    val metaInfo: DigitalTwinMetaInfo
    val modelData: PhysicalCounterpartModelData
    val evolutionManager: EvolutionController
    /**
     * Add a semantic link to another digital twin
     * */
    fun addLink(digitalTwinId: URI, semantic: LinkSemantic)

    /**
     * Delete a semantic link to another digital twin if present
     * */
    fun deleteLink(digitalTwinId: URI, semantic: LinkSemantic)

    /**
     * Stopping the digital twin causes the termination of the evolution of the digital twin and the resource release
     * */
    fun stop()
}

enum class DigitalTwinState {
    IN_SYNC,
    NOT_IN_SYNC,
    STOPPED
}


interface DigitalTwinMetaInfo

interface LinkSemantic

data class basicSemantic(val description: String) : LinkSemantic

data class DigitalTwinValue<T>(val value: T, val generationTime: Instant)

/**
 * Classes that implements this interface model physical counterpart information,
 * that the digital twin must store and manage such as: Physiological, structural, behavioural information.
 * */
interface PhysicalCounterpartModelData


interface PhysiologicalModel : PhysicalCounterpartModelData
interface StructuralModel : PhysicalCounterpartModelData
interface BehaviouralModel : PhysicalCounterpartModelData
interface ProcessModel : PhysicalCounterpartModelData

/*
* This component encapsulate the interaction with the physical counter part in order to maintain the physical model updated,
* It also should react and update the physical model if the some interaction with other digital twin could reflect an interaction
* in the physical world that could have consequences on the physical counterpart
* */
interface EvolutionController {
    fun start()
    fun stop()
}


interface CommunicationAdapter


interface DigitalTwinFactory {
    fun create(id: URI): DigitalTwin
}