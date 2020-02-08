package hospitaldigitaltwins.prehmanagement.eventmanagement

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * Created by Matteo Gabellini on 06/02/2020.
 */
data class EventInfo(
        @JsonProperty("callTime") val callTime: Date,
        @JsonProperty("address") val address: String,
        @JsonProperty("dispatchCode") val dispatchCode: String,
        @JsonProperty("emergencyType") val emergencyType: String,
        @JsonProperty("dynamic") val dynamic: String,
        @JsonProperty("patientsNumber") val patientsNumber: Int
)
