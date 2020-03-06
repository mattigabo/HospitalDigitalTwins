package digitaltwinframework.coreimplementation

/**
 * Created by Matteo Gabellini on 24/01/2020.
 */
interface Semantics

interface HasSemantics {
    val semantics: Semantics
}

interface DigitalTwinRelation : HasSemantics {
    val otherDigitalTwin: String
}

data class TextualSemantics(val description: String) : Semantics
