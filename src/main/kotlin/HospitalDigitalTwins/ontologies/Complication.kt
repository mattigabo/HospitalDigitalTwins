package HospitalDigitalTwins.ontologies

import java.util.*

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */
data class Complication(val name: String, val note: String = "", val generationTime: Date)

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


}