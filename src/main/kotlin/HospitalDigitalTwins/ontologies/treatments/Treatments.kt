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

open class BasicTreatment(val executionTime: Date, override var location: String) : Treatment

open class TimedTreatment(val startTime: Date, override var location: String) : Treatment {
    var endTime: Date? = null
}

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