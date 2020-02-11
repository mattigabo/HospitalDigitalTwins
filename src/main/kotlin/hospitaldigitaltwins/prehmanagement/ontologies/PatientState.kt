package hospitaldigitaltwins.prehmanagement.ontologies

/**
 * Created by Matteo Gabellini on 28/01/2020.
 */
class PatientState(
    var traumaType: TraumaTypes = TraumaTypes.NO_TRAUMA,
    var helmetSafetyBeltPresent: Boolean = false,
    var externalBleeding: Boolean = false,
    var perviousAirways: Boolean = false,
    var tachipneaDispnea: Boolean = false,
    var costalChestDeformity: Boolean = false,
    var ecofast: Boolean = false,
    var unstablePelvis: Boolean = false,
    var skullFracture: Boolean = false,
    var paraparesis: Boolean = false,
    var tetraparesis: Boolean = false,
    var paresthesia: Boolean = false,
    var shockIndex: Boolean = false
)

enum class TraumaTypes(val stringValue: String) {
    MAJOR_TRAUMA("Trauma Maggiore"),
    CLOSED_TRAUMA("Trauma Chiuso"),
    PIERCING_TRAUMA("Trauma Penetrante"),
    AMPUTATION("Sub-amputation/amputation"),
    NO_TRAUMA("");

    override fun toString(): String {
        return this.stringValue
    }
}