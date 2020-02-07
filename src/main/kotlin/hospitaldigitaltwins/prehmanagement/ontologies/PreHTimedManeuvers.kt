package hospitaldigitaltwins.prehmanagement.ontologies

import hospitaldigitaltwins.ontologies.procedures.ProcedureFactory
import hospitaldigitaltwins.ontologies.procedures.TimedManeuver
import java.util.*

enum class PreHTimedManeuvers(val stringFormat: String) : ProcedureFactory<TimedManeuver> {
    CARDIO_PULMONARY_RESUSCITATION("cardio pulmonary resuscitation"),
    TOURNIQUET("tourniquet"),
    REBOA_ZONE_1("reboa zone 1"),
    REBOA_ZONE_3("reboa zone 3");

    override fun create(executionTime: Date): TimedManeuver {
        return TimedManeuver(this.stringFormat, executionTime)
    }
}