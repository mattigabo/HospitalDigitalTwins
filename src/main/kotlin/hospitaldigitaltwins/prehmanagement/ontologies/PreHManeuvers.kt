package hospitaldigitaltwins.prehmanagement.ontologies

import hospitaldigitaltwins.ontologies.procedures.Maneuver
import hospitaldigitaltwins.ontologies.procedures.ManeuverFactory
import java.util.*

/*
*  List of the Pre Hospital Maneuvers, element of this enum expose a factory method to create instances of the Maneuver class
*
* */
enum class PreHManeuvers(override val stringFormat: String) : ManeuverFactory {
    CERVICAL_COLLAR("cervical-collar"),
    IMMOBILIZATION("immobilization"),
    SYNC_ELECTRICAL_CARDIOVERSION("synchronized-electrical-cardioversion"),
    GASTRIC_PROBE("gastric-probe"),
    BLADDER_PROBE("bladder-probe");

    override fun toString(): String {
        return this.stringFormat
    }
}

class PacingManeuver(
    captureRateInBpm: Int,
    amperageInMillisAmps: Double,
    executionTime: Date,
    location: String
) : Maneuver("pacing-maneuver", executionTime)