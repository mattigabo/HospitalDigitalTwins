package hospitaldigitaltwins.traumamanagement.ontologies

import hospitaldigitaltwins.ontologies.procedures.Administration
import java.util.*

/**
 * Created by Matteo Gabellini on 03/02/2020.
 */
class ContinuousInfusion(
    drug: String,
    val startTime: Date,
    var dosage: Int,
    var endTime: Date
) : Administration(drug, startTime)

enum class TraumaInfusion(val stringFormat: String) {
    CRYSTALLOID("Cristalloidi"),
    HYPERTONIC_SOLUTION("Soluzione Ipertonica");

    override fun toString(): String {
        return this.stringFormat
    }

    /**
     * Factory method that create a complication instance with the complication name item from which the method was invoked
     * */
    fun occurs(note: String = "", occurrenceTime: Date): Administration {
        return Administration(
            this.stringFormat,
            occurrenceTime
        )
    }
}

enum class TraumaGenericDrug(val stringFormat: String) {
    SUCCINYLCHOLINE("Succinilcolina"),
    KETAMINE("Ketamine"),
    CURARE("Curaro"),
    TRANEXAMIC_ACID("Acido Tranexamico"),
    FENTANYL("Fentanil"),
    MIDAZOLAM("Midazolam"),
    MANNITOL("Mannitolo"),
    THIOPENTAL("Tiopentone");

    override fun toString(): String {
        return this.stringFormat
    }

    /**
     * Factory method that create a complication instance with the complication name item from which the method was invoked
     * */
    fun occurs(occurrenceTime: Date): Administration {
        return Administration(
            this.stringFormat,
            occurrenceTime
        )
    }
}