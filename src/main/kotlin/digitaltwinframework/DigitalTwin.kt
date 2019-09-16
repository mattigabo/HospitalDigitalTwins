package digitaltwinframework

import java.net.URI

interface DigitalTwin {
    val identifier: URI
    var metaInfo: ArrayList<DigitalTwinMetaInfo>
    var modelData: PhysicalCounterpartModelData
    val evolutionManager: EvolutionManager
}

interface DigitalTwinMetaInfo

interface LinkSemantic

class basicSemantic(description: String) : LinkSemantic

class LinkToDigitalTwin(val otherDtMetaInfo: DigitalTwinMetaInfo, val semantic: LinkSemantic)

interface DigitalTwinInfo<T> {
    val value: T
    val generationTime: Long
}

/**
 * Classes that implements this interface model physical counterpart infomations,
 * that the digital twin must store and manage such as: Physiological, structural, behavioural information.
 * */
interface PhysicalCounterpartModelData

interface PhysiologicalModel : PhysicalCounterpartModelData
interface StructuralModel : PhysicalCounterpartModelData
interface BehaviouralModel : PhysicalCounterpartModelData
interface ProcessModel : PhysicalCounterpartModelData

/*
* This component encapsulate the interaction with the physical counter part in order to mantain the physical model updated,
* It also should react and update the physical model if the some interaction with other digital twin could reflect an interaction
* in the physical world that could have consequences on the physical counterpart
* */
interface EvolutionManager


interface CommunicationAdapter