package HospitalDigitalTwins.ontologies

import java.util.*

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */
data class Anagraphic(var name: String,
                      var surname: String,
                      var residency: String,
                      var birthplace: String,
                      var birthDate: Date,
                      var gender: Gender)

enum class Gender(val acronym: String, val stringValue: String) {
    MALE("M", "male"),
    FEMALE("F", "female");

    override fun toString(): String {
        return this.stringValue
    }
}