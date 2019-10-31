package hospitaldigitaltwins.ontologies

class BloodType(var group: Blood.Group, var rh: Blood.RhFactor)

object Blood {
    enum class Group(private val stringFormat: String) {
        A("A"),
        B("B"),
        AB("AB"),
        Zero("0");

        override fun toString(): String {
            return this.stringFormat
        }
    }

    enum class RhFactor(val signFormat: String) {
        POSITIVE("+"),
        NEGATIVE("-")
    }
}