package hospitaldigitaltwins.prehmanagement.ontologies

import hospitaldigitaltwins.ontologies.procedures.Maneuver
import hospitaldigitaltwins.ontologies.procedures.ProcedureFactory
import java.util.*

/*
*  List of the Pre Hospital Maneuvers, element of this enum expose a factory method to create instances of the Maneuver class
*
* */
object PreHManeuvers {
    enum class PreHSimpleManeuvers(val stringFormat: String) : ProcedureFactory<Maneuver> {
        CERVICAL_COLLAR("cervical-collar"),
        IMMOBILIZATION("immobilization"),
        SYNC_ELECTRICAL_CARDIOVERSION("synchronized-electrical-cardioversion"),
        GASTRIC_PROBE("gastric-probe"),
        BLADDER_PROBE("bladder-probe"),
        JAW_SUBLUXATION("jaw-subluxation"),
        GUEDEL("guedel"),
        CRICOTHYROTOMY("cricothyrotomy"),
        TRACHEAL_TUBE("tracheal-tube"),
        OXYGEN_THERAPY("oxygen-therapy"),
        AMBU("ambu"),
        MINITHORACOTOMYLEFT("mini-thoracotomy-left"),
        MINITHORACOTOMYRIGHT("mini-thoracotomy-right"),
        HEMOSTASIS("haemostasis"),
        PELVIC_BINDER("pelvic-binder"),
        NEURO_PROTECTION("neuro-protection"),
        THERMAL_PROTECTION("thermal-protection");

        override fun create(executionTime: Date): Maneuver {
            return Maneuver(this.stringFormat, executionTime)
        }

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

    class VenousWays(
        var typology: VenousWays.Typology,
        val calimber: String,
        override val executionTime: Date
    ) : Maneuver("${typology} injection", executionTime) {

        enum class Typology(val stringFormat: String) {
            PERIPHERAL("peripheral"),
            CENTRAL("central"),
            INTREOSSEUS("intreosseus");

            override fun toString(): String {
                return this.toString()
            }
        }
    }

    class OxygenTherapy(
        override val executionTime: Date,
        val dosage: Double
    ) : Maneuver("oxygen-therapy", executionTime)
}