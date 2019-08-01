package HospitalDigitalTwins.ontologies

import HospitalDigitalTwins.ontologies.treatments.Treatment

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */
class ClinicalData(var bloodType: BloodType, var vitalParameters: MutableList<VitalParameter<Any>>, var treatments: MutableList<Treatment>)

class BloodType(var group: BloodGroup, var rh: RhFactor)

enum class BloodGroup(private val stringFormat: String) {
    A("A"), B("B"), AB("AB"), Zero("0");

    override fun toString(): String {
        return this.stringFormat
    }
}

enum class RhFactor(val signFormat: String) {
    POSITIVE("+"), NEGATIVE("-")
}

