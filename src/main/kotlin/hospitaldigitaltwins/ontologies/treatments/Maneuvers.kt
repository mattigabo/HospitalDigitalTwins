package hospitaldigitaltwins.ontologies.treatments

import java.util.*

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */
open class Maneuver(override val name: String) : Treatment

class PacingManeuver(
        captureRateInBpm: Int,
        amperageInMillisAmps: Double,
        override val executionTime: Date,
        location: String
) : Maneuver("pacing-maneuver"), BasicTreatment

interface ManeuverFactory {
    val stringFormat: String
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

object BasicManeuver {

    fun shock(executionTime: Date): Maneuver = object : Maneuver("shock"), BasicTreatment {
        override val executionTime = executionTime
    }

    fun jawSubluxation(executionTime: Date): Maneuver = object : Maneuver("jaw-subluxation"), BasicTreatment {
        override val executionTime = executionTime
    }

    fun guedel(executionTime: Date): Maneuver = object : Maneuver("guedel"), BasicTreatment {
        override val executionTime = executionTime
    }

    fun cricothyrotomy(executionTime: Date): Maneuver = object : Maneuver("cricothyrotomy"), BasicTreatment {
        override val executionTime = executionTime
    }

    fun trachealTube(executionTime: Date): Maneuver = object : Maneuver("tracheal-tube"), BasicTreatment {
        override val executionTime = executionTime
    }

    fun oxygenTherapy(executionTime: Date): Maneuver = object : Maneuver("oxygen-therapy"), BasicTreatment {
        override val executionTime = executionTime
    }

    fun ambu(executionTime: Date): Maneuver = object : Maneuver("ambu"), BasicTreatment {
        override val executionTime = executionTime
    }

    fun miniThoracotomyLeft(executionTime: Date): Maneuver = object : Maneuver("mini-thoracotomy-left"), BasicTreatment {
        override val executionTime = executionTime
    }

    fun miniThoracotomyRight(executionTime: Date): Maneuver = object : Maneuver("mini-thoracotomy-right"), BasicTreatment {
        override val executionTime = executionTime
    }

    fun haemostasis(executionTime: Date): Maneuver = object : Maneuver("haemostasis"), BasicTreatment {
        override val executionTime = executionTime
    }

    fun pelvicBinder(executionTime: Date): Maneuver = object : Maneuver("pelvic-binder"), BasicTreatment {
        override val executionTime = executionTime
    }

    fun neuroprotection(executionTime: Date): Maneuver = object : Maneuver("neuroprotection"), BasicTreatment {
        override val executionTime = executionTime
    }

    fun thermalProtection(executionTime: Date): Maneuver = object : Maneuver("thermal-protection"), BasicTreatment {
        override val executionTime = executionTime
    }


}

object TimedMeneuver {
    fun reanimation(
            startTime: Date,
            endTime: Date? = null
    ): Maneuver = object : Maneuver("reanimation"), TimedTreatment {
        override val startTime = startTime
        override var endTime = endTime
    }

    fun tourniquet(
            startTime: Date,
            endTime: Date? = null
    ): Maneuver = object : Maneuver("tourniquet"), TimedTreatment {
        override val name = "reboa-zone-3"
        override val startTime = startTime
        override var endTime = endTime
    }

    fun reboaZone1(
            startTime: Date,
            endTime: Date? = null
    ): Maneuver = object : Maneuver("reboa-zone-1"), TimedTreatment {
        override val name = "reboa-zone-3"
        override val startTime = startTime
        override var endTime = endTime
    }

    fun reboaZone3(
            startTime: Date,
            endTime: Date? = null
    ): Maneuver = object : Maneuver("reboa-zone-3"), TimedTreatment {
        override val name = "reboa-zone-3"
        override val startTime = startTime
        override var endTime = endTime
    }

}