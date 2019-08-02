package HospitalDigitalTwins.ontologies.treatments

import HospitalDigitalTwins.ontologies.Drug
import java.util.*

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */

/**
 *
 * @param executionTime
 * @param location
 */
interface Treatment {
    val location: kotlin.String
}

abstract class BasicTreatment(open val executionTime: Date, override var location: String) : Treatment

abstract class TimedTreatment(
        val name: String,
        open val startTime: Date,
        override val location: String,
        open var endTime: Date? = null
) : Treatment

class Administration(val drug: Drug, executionTime: Date, location: String) : BasicTreatment(executionTime, location)

class Injection(
    var typology: Injection.Typology,
    val calimber: String,
    executionTime: Date,
    location: String
) : BasicTreatment(executionTime, location) {

    enum class Typology(val stringFormat: String) {
        PERIPHERAL("peripheral"),
        CENTRAL("central"),
        INTREOSSEUS("intreosseus");

        override fun toString(): String {
            return this.toString()
        }
    }
}


object TimedTreatments {
    data class Reanimation(
            override val startTime: Date,
            override val location: String,
            override var endTime: Date? = null
    ) : TimedTreatment("reboa-zone-3", startTime, location, endTime)

    data class Tourniquet(
            override val startTime: Date,
            override val location: String,
            override var endTime: Date? = null
    ) : TimedTreatment("reboa-zone-3", startTime, location, endTime)

    data class ReboaZone1(
            override val startTime: Date,
            override val location: String,
            override var endTime: Date? = null
    ) : TimedTreatment("reboa-zone-3", startTime, location, endTime)

    data class ReboaZone3(
            override val startTime: Date,
            override val location: String,
            override var endTime: Date? = null
    ) : TimedTreatment("reboa-zone-3", startTime, location, endTime)
}