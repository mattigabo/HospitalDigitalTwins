package hospitaldigitaltwins.prehmanagement.ontologies

import hospitaldigitaltwins.ontologies.procedures.Administration
import java.util.*

/**
 * Created by Matteo Gabellini on 28/01/2020.
 */
enum class PreHInfusion(val stringFormat: String) {
    CRYSTALLOID("Cristalloidi"),
    MANNITOL("Mannitolo"),
    HYPERTONIC_SOLUTION("Soluzione Ipertonica"),
    CONCENTRATED_RED_BLOOD_CELLS("Globuli rossi concentrati"),
    FIBRINOGEN("Fibrinogeno");

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

enum class PreHGenericDrug(val drugName: String) {
    SUCCINYLCHOLINE("Succinilcolina"),
    KETAMINE("Ketamine"),
    CURARE("Curaro"),
    TRANEXAMIC_ACID("Acido Tranexamico"),
    FENTANYL("Fentanil"),
    MIDAZOLAM("Midazolam");

    override fun toString(): String {
        return this.drugName
    }

    /**
     * Factory method that create a drug administration instance with the drug name item from which the method was invoked
     * */
    fun occurs(occurrenceTime: Date): Administration {
        return Administration(
            this.drugName,
            occurrenceTime
        )
    }
}

