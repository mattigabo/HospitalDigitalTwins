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
    val name: String
}

interface BasicTreatment : Treatment {
    val executionTime: Date
}

interface TimedTreatment : Treatment {
    val startTime: Date
    var endTime: Date?
}

class Administration(val drug: Drug, override val executionTime: Date) : BasicTreatment {
    override val name: String = "Administration of ${this.drug.name}"
}

class Injection(
        var typology: Injection.Typology,
        val calimber: String,
        override val executionTime: Date
) : BasicTreatment {

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

class IppvTreatment(
        val vt: Int,
        val fr: Int,
        val peep: Int,
        val fio2: Int,
        override val executionTime: Date
) : BasicTreatment {
    override val name = "IppvTreatment"
}

data class Adrenalin(override val executionTime: Date) : BasicTreatment {
    override val name = "adrenalin"
}