package hospitaldigitaltwins.prehmanagement.ontologies

import hospitaldigitaltwins.ontologies.procedures.TimedTreatment
import hospitaldigitaltwins.ontologies.procedures.Treatment
import java.util.*

/**
 * Created by Matteo Gabellini on 28/01/2020.
 */
enum class BasicTreatments(val stringFormat: String) {

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
    THERMAL_PROTECTION("thermal-protection"),

    fun create(executionTime: Date): Treatment {
        return Treatment(this.stringFormat, executionTime)
    }

}

object TimedTreatment {
    CARDIO_PULMONARY_RESUSCITATION
    fun reanimation(
        startTime: Date,
        endTime: Date? = null
    ): Treatment = object : Treatment("reanimation"), TimedTreatment {
        override val startTime = startTime
        override var endTime = endTime
    }

    fun tourniquet(
        startTime: Date,
        endTime: Date? = null
    ): Treatment = object : Treatment("tourniquet"), TimedTreatment {
        override val name = "reboa-zone-3"
        override val startTime = startTime
        override var endTime = endTime
    }

    fun reboaZone1(
        startTime: Date,
        endTime: Date? = null
    ): Treatment = object : Treatment("reboa-zone-1"), TimedTreatment {
        override val name = "reboa-zone-3"
        override val startTime = startTime
        override var endTime = endTime
    }

    fun reboaZone3(
        startTime: Date,
        endTime: Date? = null
    ): Treatment = object : Treatment("reboa-zone-3"), TimedTreatment {
        override val name = "reboa-zone-3"
        override val startTime = startTime
        override var endTime = endTime
    }

    fun create(executionTime: Date, endTime: Date? = null): TimedTreatment {
        return TimedTreatment(this.stringFormat, executionTime,)
    }
}

class VenousWays(
    var typology: VenousWays.Typology,
    val calimber: String,
    override val executionTime: Date
) : Treatment {

    override val name = "${this.typology} injection"

    enum class Typology(val stringFormat: String) {
        PERIPHERAL("peripheral"),
        CENTRAL("central"),
        INTREOSSEUS("intreosseus");

        override fun toString(): String {
            return this.toString()
        }
    }
}

data class OxygenTherapy(override val executionTime: Date, val dosage: Double) : Treatment {
    override val name = "oxygen-therapy"
}