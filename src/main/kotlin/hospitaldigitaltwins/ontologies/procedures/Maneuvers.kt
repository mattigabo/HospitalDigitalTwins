package hospitaldigitaltwins.ontologies.procedures

import java.util.*

open class Maneuver(override val name: String, override val executionTime: Date) : Procedure

interface ManeuverFactory {
    val stringFormat: String
}

