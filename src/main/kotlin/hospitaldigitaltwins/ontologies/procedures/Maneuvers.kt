package hospitaldigitaltwins.ontologies.procedures

import java.util.*

open class Maneuver(override val name: String, override val executionTime: Date) : Procedure

open class TimedManeuver(
    override val name: String,
    override val executionTime: Date,
    override var endTime: Date? = null
) : TimedProcedure, Maneuver(name, executionTime)


