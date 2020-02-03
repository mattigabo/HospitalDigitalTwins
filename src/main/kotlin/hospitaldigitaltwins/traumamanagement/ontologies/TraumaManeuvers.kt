package hospitaldigitaltwins.traumamanagement.ontologies

import hospitaldigitaltwins.ontologies.procedures.ManeuverFactory

/**
 * Created by Matteo Gabellini on 28/01/2020.
 */
enum class TraumaManeuvers(override val stringFormat: String) : ManeuverFactory {
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
}