package digitaltwinframework.coreimplementation

/**
 * Created by Matteo Gabellini on 24/01/2020.
 */
interface Semantics

interface HasSemantics {
    var semantics: List<Semantics>
}

abstract class SemanticRelation<T> : HasSemantics {
    abstract var first: T
    abstract var second: T
}

data class textualSemantics(val description: String) : Semantics

abstract class DigitalTwinRelation<URI>