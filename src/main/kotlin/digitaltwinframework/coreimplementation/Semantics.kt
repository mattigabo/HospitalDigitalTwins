package digitaltwinframework.coreimplementation

import java.net.URI

/**
 * Created by Matteo Gabellini on 24/01/2020.
 */
interface Semantics

interface HasSemantics {
    var semantics: List<Semantics>
}

abstract class DigitalTwinRelation : HasSemantics {
    abstract var otherDTIReference: URI
}

data class TextualSemantics(val description: String) : Semantics
