package hospitaldigitaltwins.traumamanagement.ontologies

import hospitaldigitaltwins.ontologies.procedures.Maneuver
import hospitaldigitaltwins.ontologies.procedures.ProcedureFactory
import java.util.*


/**
 * Created by Matteo Gabellini on 28/01/2020.
 */
enum class TraumaManeuvers(val stringFormat: String) : ProcedureFactory<Maneuver> {
    OROTRACHEAL_INTUBATION("orotracheal-intubation"),
    PELVIC_BINDER("pelvic-binder"),
    PRESSURE_INFUSER("pressure-infuser"),
    HEMORRHAGIC_WOUND_SUTURE("hemorrhagic-wound-suture"),
    RESUSCITATIVE_THORACOTOMY("resuscitative-thoracotomy"),
    THORACIC_DRAINAGE("thoracic-drainage"),
    HIGH_FLOW_CATHETER("high-flow-catheter"),
    EXTERNAL_FIXATOR_POSITIONING("external-fixator-positioning"),
    REBOA("reboa"),
    TPOD("tpod");

    override fun toString(): String {
        return this.stringFormat
    }

    override fun create(executionTime: Date): Maneuver {
        return Maneuver(this.name, executionTime)
    }

}