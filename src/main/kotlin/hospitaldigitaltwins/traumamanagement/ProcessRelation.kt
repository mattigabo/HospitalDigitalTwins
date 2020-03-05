package hospitaldigitaltwins.traumamanagement

import com.fasterxml.jackson.annotation.JsonProperty
import digitaltwinframework.coreimplementation.DigitalTwinRelation
import digitaltwinframework.coreimplementation.Semantics
import java.net.URI

/**
 * Created by Matteo Gabellini on 05/03/2020.
 */
data class ProcessRelation(override var otherDTIReference: URI, val relationSemantics: ManageAPatientFrom) :
    DigitalTwinRelation() {
    override var semantics: List<Semantics> = listOf(relationSemantics)
}

data class ManageAPatientFrom(
    @JsonProperty("description") val description: String = "Manage a patient from",
    @JsonProperty("missionId") val missionId: String,
    @JsonProperty("restAddress") val restAddress: String
) : Semantics
