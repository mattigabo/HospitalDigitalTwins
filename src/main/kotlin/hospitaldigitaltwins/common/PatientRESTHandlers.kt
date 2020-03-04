package hospitaldigitaltwins.common

import digitaltwinframework.coreimplementation.restmanagement.AbstractRestHandlers
import digitaltwinframework.coreimplementation.restmanagement.RESTDefaultResponse
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages
import io.vertx.core.Handler
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext

object PatientOperationIds {
    val GET_PATIENT = "getPatientInfo"

    val UPDATE_MEDICAL_HISTORY = "setMedicalHistory"
    val GET_MEDICAL_HISTORY = "getMedicalHistory"

    val UPDATE_ANAGRAPHIC = "updateAnagraphic"
    val GET_ANAGRAPHIC = "getAnagraphic"

    val UPDATE_STATUS = "updateStatus"
    val GET_STATUS = "getStatus"


    val GET_VITALPARAMETERS_HISTORY = "getVitalParametersHistory"
    val ADD_VITALPARAMETERS = "addVitalParameters"
    val GET_VITALPARAMETERS = "getCurrentVitalParameters"

    val GET_VITALPARAMETER = "getVitalParameter"
    val GET_VITALPARAMETER_HISTORY = "getVitalParameterHistory"

    val GET_ALL_ADMINISTRATION = "getAdministrations"
    val ADD_ADMINISTRATION = "addAdministration"

    val GET_EXECUTED_MANEUVERS = "getExecutedManeuvers"
    val ADD_MANEUVER = "addManeuver"

    val ADD_TIMED_MANEUVER = "addTimedManeuver"
    val UPDATE_TIMED_MANEUVER = "updateTimedManeuver"
    val GET_TIMED_MANEUVER = "getTimedManeuver"

    val GET_ALL_COMPLICATIONS = "getAllComplications"
    val ADD_COMPLICATION = "addComplication"
    val GET_COMPLICATION = "getComplication"

    val GET_ALL_NOTES = "getAllNotes"
    val ADD_TEXT_NOTE = "addTextNote"
    val ADD_AUDIO_NOTE = "addAudioNote"
    val ADD_PHOTO_NOTE = "addPhoto"
    val ADD_VIDEO_NOTE = "addVideo"

    val GET_TEXT_NOTE = "getTextNote"
    val GET_AUDIO_NOTE = "getAudioNote"
    val GET_PHOTO_NOTE = "getPhotos"
    val GET_VIDEO_NOTE = "getVideos"
}

object PatientRESTHandlers : AbstractRestHandlers() {
    val onGetPatient = Handler<RoutingContext> { routingContext ->
        checkIdAndPerformRequest<JsonObject>(
            routingContext,
            PatientOperationIds.GET_PATIENT,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onUpdateMedicalHistory = Handler<RoutingContext> { routingContext ->
        val medicalHistory = routingContext.bodyAsJson
        checkIdAndPerformRequest<JsonArray>(
            routingContext,
            PatientOperationIds.UPDATE_MEDICAL_HISTORY,
            medicalHistory
        )
    }

    val onGetMedicalHistory = Handler<RoutingContext> { routingContext ->
        checkIdAndPerformRequest<JsonObject>(
            routingContext,
            PatientOperationIds.GET_MEDICAL_HISTORY,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onUpdateAnagraphic = Handler<RoutingContext> { routingContext ->
        val anagraphic = routingContext.bodyAsJson
        checkIdAndPerformRequest<JsonArray>(
            routingContext,
            PatientOperationIds.UPDATE_ANAGRAPHIC,
            anagraphic
        )
    }

    val onGetAnagraphic = Handler<RoutingContext> { routingContext ->
        checkIdAndPerformRequest<JsonObject>(
            routingContext,
            PatientOperationIds.GET_ANAGRAPHIC,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onUpdateStatus = Handler<RoutingContext> { routingContext ->
        val status = routingContext.bodyAsJson
        checkIdAndPerformRequest<JsonArray>(
            routingContext,
            PatientOperationIds.UPDATE_STATUS,
            status
        )
    }

    val onGetStatus = Handler<RoutingContext> { routingContext ->
        checkIdAndPerformRequest<JsonObject>(
            routingContext,
            PatientOperationIds.GET_STATUS,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onGetVitalParametersHistory = Handler<RoutingContext> { routingContext ->
        checkIdAndPerformRequest<JsonArray>(
            routingContext,
            PatientOperationIds.GET_VITALPARAMETERS_HISTORY,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onAddVitalParameters = Handler<RoutingContext> { routingContext ->
        val vitalParameters = routingContext.bodyAsJsonArray
        checkIdAndPerformRequest<JsonArray>(
            routingContext,
            PatientOperationIds.ADD_VITALPARAMETERS,
            vitalParameters
        )
    }

    val onGetCurrentVitalParameters = Handler<RoutingContext> { routingContext ->
        checkIdAndPerformRequest<JsonArray>(
            routingContext,
            PatientOperationIds.GET_VITALPARAMETERS,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onGetCurrentVitalParameter = Handler<RoutingContext> { routingContext ->
        routingContext.pathParams().get("vitalParametersName")?.let {
            checkIdAndPerformRequest<String>(
                routingContext,
                PatientOperationIds.GET_VITALPARAMETER,
                it
            )
        } ?: RESTDefaultResponse.sendBadRequestResponse("vitalParametersName not specified", routingContext)
    }

    val onGetVitalParameterHistory = Handler<RoutingContext> { routingContext ->
        routingContext.pathParams().get("vitalParametersName")?.let {
            checkIdAndPerformRequest<String>(
                routingContext,
                PatientOperationIds.GET_VITALPARAMETER_HISTORY,
                it
            )
        } ?: RESTDefaultResponse.sendBadRequestResponse("vitalParametersName not specified", routingContext)
    }

    val onGetAdministrations = Handler<RoutingContext> { routingContext ->
        checkIdAndPerformRequest<JsonArray>(
            routingContext,
            PatientOperationIds.GET_ALL_ADMINISTRATION,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onAddAdministration = Handler<RoutingContext> { routingContext ->
        val administration = routingContext.bodyAsJson
        checkIdAndPerformRequest<JsonObject>(
            routingContext,
            PatientOperationIds.ADD_ADMINISTRATION,
            administration
        )
    }

    val onGetExecutedManeuvers = Handler<RoutingContext> { routingContext ->
        checkIdAndPerformRequest<JsonArray>(
            routingContext,
            PatientOperationIds.GET_EXECUTED_MANEUVERS,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onAddManeuver = Handler<RoutingContext> { routingContext ->
        val maneuver = routingContext.bodyAsJson
        checkIdAndPerformRequest<JsonObject>(
            routingContext,
            PatientOperationIds.ADD_MANEUVER,
            maneuver
        )
    }

    val onGetTimedManeuver = Handler<RoutingContext> { routingContext ->
        routingContext.pathParams().get("maneuverId")?.let {
            checkIdAndPerformRequest<String>(
                routingContext,
                PatientOperationIds.GET_EXECUTED_MANEUVERS,
                it
            )
        } ?: RESTDefaultResponse.sendBadRequestResponse("Maneuver ID not specified", routingContext)
    }

    val onAddTimedManeuver = Handler<RoutingContext> { routingContext ->
        val timedManeuver = routingContext.bodyAsJson
        checkIdAndPerformRequest<JsonObject>(
            routingContext,
            PatientOperationIds.ADD_MANEUVER,
            timedManeuver
        )
    }

    val onUpdateTimedManeuver = Handler<RoutingContext> { routingContext ->
        routingContext.pathParams().get("maneuverId")?.let {
            val timedManeuver = routingContext.bodyAsJson
            timedManeuver.put("maneuverId", it)
            checkIdAndPerformRequest<JsonObject>(
                routingContext,
                PatientOperationIds.UPDATE_TIMED_MANEUVER,
                timedManeuver
            )
        } ?: RESTDefaultResponse.sendBadRequestResponse("Maneuver ID not specified", routingContext)
    }

    private fun <T> checkIdAndPerformRequest(routingContext: RoutingContext, busAdrr: String, message: Any) {
        routingContext.pathParams().get("missionId")?.let {

            eb.request<T>(busAdrr + it, message, responseCallBack(routingContext))

        } ?: RESTDefaultResponse.sendBadRequestResponse("MissionId not specified", routingContext)
    }
}