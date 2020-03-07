package hospitaldigitaltwins.prehmanagement.ontologies

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Matteo Gabellini on 28/01/2020.
 */
class PatientState(
    @JsonProperty("traumaType") var traumaType: String = TraumaTypes.NO_TRAUMA.toString(),
    @JsonProperty("helmetSeatbelt") var helmetSafetyBeltPresent: Boolean = false,
    @JsonProperty("externalBleeding") var externalBleeding: Boolean = false,
    @JsonProperty("perviousAirways") var perviousAirways: Boolean = false,
    @JsonProperty("tachypneaDyspnea") var tachipneaDyspnea: Boolean = false,
    @JsonProperty("thoraxDeformities") var thoraxDeformities: Boolean = false,
    @JsonProperty("ecofast") var ecofast: Boolean = false,
    @JsonProperty("deformedPelvis") var deformedPelvis: Boolean = false,
    @JsonProperty("skullFracture") var skullFracture: Boolean = false,
    @JsonProperty("paraparesis") var paraparesis: Boolean = false,
    @JsonProperty("tetraparesis") var tetraparesis: Boolean = false,
    @JsonProperty("paraesthesia") var paresthesia: Boolean = false
)

enum class TraumaTypes(@JsonProperty("stringValue") val stringValue: String) {
    MAJOR_TRAUMA("Trauma Maggiore"),
    CLOSED_TRAUMA("Trauma Chiuso"),
    PIERCING_TRAUMA("Trauma Penetrante"),
    AMPUTATION("Sub-amputation/amputation"),
    NO_TRAUMA("");

    override fun toString(): String {
        return this.stringValue
    }
}