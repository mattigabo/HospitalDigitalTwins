package HospitalDigitalTwins.ontologies.treatments

import java.util.*

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */
open class Maneuver(val name: String, executionTime: Date, location: String) : BasicTreatment(executionTime, location)

class PacingManeuver(
    captureRateInBpm: Int,
    amperageInMillisAmps: Double,
    executionTime: Date,
    location: String
) : Maneuver("pacing-maneuver", executionTime, location)

interface ManeuverFactory {
    val stringFormat: String

    fun getMeneuver(executionTime: Date, location: String): Maneuver {
        return Maneuver(this.stringFormat, executionTime, location)
    }
}

/*
*  List of the Pre Hospital Maneuvers, element of this enum expose a factory method to create instances of the Maneuver class
*
* */
enum class PreHManeuvers(override val stringFormat: String) : ManeuverFactory {
    CERVICAL_COLLAR("cervical-collar"),
    IMMOBILIZATION("immobilization"),
    ELECTRICAL_CARDIOVERSION("electrical-cardioversion"),
    GASTRIC_PROBE("gastric-probe"),
    BLADDER_CATHETER("bladder-catheter");

    override fun toString(): String {
        return this.stringFormat
    }
}

enum class TraumaTeamManeuvers(override val stringFormat: String) : ManeuverFactory {
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