package hospitaldigitaltwins.ontologies

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Matteo Gabellini on 28/01/2020.
 */
class MedicalHistory(
    @JsonProperty("anticoagulants") var anticoagulants: Boolean = false,
    @JsonProperty("antiplatelets") var antiplatelets: Boolean = false,
    @JsonProperty("nao") var nao: Boolean = false
)