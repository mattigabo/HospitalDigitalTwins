package hospitaldigitaltwins.ontologies

import java.util.*

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */
class Anagraphic(
    var name: String = "",
    var surname: String = "",
    var residency: String = "",
    var birthPlace: String = "",
    var birthDate: Date = Date(),
    var gender: Gender = Gender.MALE
)

enum class Gender(val acronym: String, val stringValue: String) {
    MALE("M", "male"),
    FEMALE("F", "female");

    override fun toString(): String {
        return this.stringValue
    }
}