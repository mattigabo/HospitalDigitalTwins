package hospitaldigitaltwins.ontologies

import java.util.*

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */
data class Complication(val name: String, val note: String = "", val occurrenceTime: Date)

enum class ComplicationNames(val stringFormat: String) {
    IMPAIRED_CONSCIOUSNESS("impaired-consciousness"),
    ANISOCORIA_MYDRIASIS("anisocoria-mydriasis"),
    RESPIRATORY_FAILURE("respiratory-failure"),
    CARDIOCIRCULATORY_SHOCK("cardiocirculatory-shock"),
    LANDING_IN_ITINERE("landing-in-itinere"),
    DEATH_IN_ITINERE("death-in-itinere"),
    DEATH_IN_PS("death-in-ps");

    override fun toString(): String {
        return this.stringFormat
    }

    /**
     * Factory method that create a complication instance with the complication name item from which the method was invoked
     * */
    fun occurs(note: String = "", occurrenceTime: Date): Complication {
        return Complication(this.stringFormat, note, occurrenceTime)
    }
}