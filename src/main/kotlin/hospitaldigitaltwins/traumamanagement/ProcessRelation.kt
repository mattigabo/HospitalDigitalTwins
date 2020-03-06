package hospitaldigitaltwins.traumamanagement

import com.fasterxml.jackson.annotation.JsonProperty
import digitaltwinframework.coreimplementation.DigitalTwinRelation
import digitaltwinframework.coreimplementation.Semantics

/**
 * Created by Matteo Gabellini on 05/03/2020.
 */
data class ProcessRelation(
    @JsonProperty("otherDigitalTwin") override val otherDigitalTwin: String = "",
    @JsonProperty("semantics") override val semantics: ManageAPatientFrom = ManageAPatientFrom()
) : DigitalTwinRelation

data class ManageAPatientFrom(
    @JsonProperty("description") val description: String = "Manage a patient from",
    @JsonProperty("missionId") val missionId: String = "",
    @JsonProperty("restHost") val restHost: String = "",
    @JsonProperty("restPort") val restPort: Int = 0
) : Semantics
