package hospitaldigitaltwins.prehmanagement.ontologies

/**
 * Created by Matteo Gabellini on 28/01/2020.
 */
class PatientState(
    var traumaType: TraumaTypes,
    var helmetSafetyBeltPresent: Boolean,
    var externalBleeding: Boolean,
    var perviousAirways: Boolean,
    var tachipneaDispnea: Boolean,
    var costalChestDeformity: Boolean,
    var ecofast: Boolean,
    var unstablePelvis: Boolean,
    var skullFracture: Boolean,
    var paraparesis: Boolean,
    var tetraparesis: Boolean,
    var paresthesia: Boolean,
    var shockIndex: Boolean
)

enum class TraumaTypes(val stringValue: String) {
    MAJOR_TRAUMA("Trauma Maggiore"),
    CLOSED_TRAUMA("Trauma Chiuso"),
    PIERCING_TRAUMA("Trauma Penetrante"),
    AMPUTATION("Sub-amputation/amputation");

    override fun toString(): String {
        return this.stringValue
    }
}