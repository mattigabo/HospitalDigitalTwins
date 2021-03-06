package digitaltwinframework.coreimplementation.utils.eventbusutils

import com.fasterxml.jackson.annotation.JsonProperty

object StandardMessages {
    const val EMPTY_MESSAGE = ""
    const val OPERATION_EXECUTED_MESSAGE = "Operation Executed"
    const val JSON_MALFORMED_MESSAGE_PREFIX = "The Json passed couldn't be trasformed in the class"
}

data class JsonResponse(@JsonProperty("operationResult") val operationResult: String)

object FailureCode {
    const val JSON_MALFORMED = 1
    const val PROBLEM_WITH_MONGODB = 2
    const val PROBLEM_IN_MISSION_CREATION = 3
    const val PROBLEM_IN_PATIENT_FIELD_UPDATE = 4
    const val PROBLEM_IN_DT_COMMUNICATION = 5
    const val PROBLEM_IN_TRAUMA_FIELD_UPDATE = 6
}