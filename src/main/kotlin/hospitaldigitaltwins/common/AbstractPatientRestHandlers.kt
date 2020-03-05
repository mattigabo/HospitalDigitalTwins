package hospitaldigitaltwins.common

import digitaltwinframework.coreimplementation.restmanagement.AbstractRestHandlers
import digitaltwinframework.coreimplementation.restmanagement.RESTDefaultResponse
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages
import io.vertx.core.Handler
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext

abstract class AbstractPatientRestHandlers : AbstractRestHandlers() {
    val onGetPatient = Handler<RoutingContext> { routingContext ->
        performRequest<JsonObject>(
            routingContext,
            PatientOperationIds.GET_PATIENT,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onUpdateMedicalHistory = Handler<RoutingContext> { routingContext ->
        val medicalHistory = routingContext.bodyAsJson
        performRequest<JsonArray>(
            routingContext,
            PatientOperationIds.UPDATE_MEDICAL_HISTORY,
            medicalHistory
        )
    }

    val onGetMedicalHistory = Handler<RoutingContext> { routingContext ->
        performRequest<JsonObject>(
            routingContext,
            PatientOperationIds.GET_MEDICAL_HISTORY,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onUpdateAnagraphic = Handler<RoutingContext> { routingContext ->
        val anagraphic = routingContext.bodyAsJson
        performRequest<JsonArray>(
            routingContext,
            PatientOperationIds.UPDATE_ANAGRAPHIC,
            anagraphic
        )
    }

    val onGetAnagraphic = Handler<RoutingContext> { routingContext ->
        performRequest<JsonObject>(
            routingContext,
            PatientOperationIds.GET_ANAGRAPHIC,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onUpdateStatus = Handler<RoutingContext> { routingContext ->
        val status = routingContext.bodyAsJson
        performRequest<JsonArray>(
            routingContext,
            PatientOperationIds.UPDATE_STATUS,
            status
        )
    }

    val onGetStatus = Handler<RoutingContext> { routingContext ->
        performRequest<JsonObject>(
            routingContext,
            PatientOperationIds.GET_STATUS,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onGetVitalParametersHistory = Handler<RoutingContext> { routingContext ->
        performRequest<JsonArray>(
            routingContext,
            PatientOperationIds.GET_VITALPARAMETERS_HISTORY,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onAddVitalParameters = Handler<RoutingContext> { routingContext ->
        val vitalParameters = routingContext.bodyAsJsonArray
        performRequest<JsonArray>(
            routingContext,
            PatientOperationIds.ADD_VITALPARAMETERS,
            vitalParameters
        )
    }

    val onGetCurrentVitalParameters = Handler<RoutingContext> { routingContext ->
        performRequest<JsonArray>(
            routingContext,
            PatientOperationIds.GET_VITALPARAMETERS,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onGetCurrentVitalParameter = Handler<RoutingContext> { routingContext ->
        routingContext.pathParams().get("vitalParametersName")?.let {
            performRequest<String>(
                routingContext,
                PatientOperationIds.GET_VITALPARAMETER,
                it
            )
        } ?: RESTDefaultResponse.sendBadRequestResponse("vitalParametersName not specified", routingContext)
    }

    val onGetVitalParameterHistory = Handler<RoutingContext> { routingContext ->
        routingContext.pathParams().get("vitalParametersName")?.let {
            performRequest<String>(
                routingContext,
                PatientOperationIds.GET_VITALPARAMETER_HISTORY,
                it
            )
        } ?: RESTDefaultResponse.sendBadRequestResponse("vitalParametersName not specified", routingContext)
    }

    val onGetAdministrations = Handler<RoutingContext> { routingContext ->
        performRequest<JsonArray>(
            routingContext,
            PatientOperationIds.GET_ALL_ADMINISTRATION,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onAddAdministration = Handler<RoutingContext> { routingContext ->
        val administration = routingContext.bodyAsJson
        performRequest<JsonObject>(
            routingContext,
            PatientOperationIds.ADD_ADMINISTRATION,
            administration
        )
    }

    val onGetExecutedManeuvers = Handler<RoutingContext> { routingContext ->
        performRequest<JsonArray>(
            routingContext,
            PatientOperationIds.GET_EXECUTED_MANEUVERS,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onAddManeuver = Handler<RoutingContext> { routingContext ->
        val maneuver = routingContext.bodyAsJson
        performRequest<JsonObject>(
            routingContext,
            PatientOperationIds.ADD_MANEUVER,
            maneuver
        )
    }

    val onGetTimedManeuver = Handler<RoutingContext> { routingContext ->
        routingContext.pathParams().get("maneuverId")?.let {
            performRequest<String>(
                routingContext,
                PatientOperationIds.GET_EXECUTED_MANEUVERS,
                it
            )
        } ?: RESTDefaultResponse.sendBadRequestResponse("Maneuver ID not specified", routingContext)
    }

    val onAddTimedManeuver = Handler<RoutingContext> { routingContext ->
        val timedManeuver = routingContext.bodyAsJson
        performRequest<JsonObject>(
            routingContext,
            PatientOperationIds.ADD_MANEUVER,
            timedManeuver
        )
    }

    val onUpdateTimedManeuver = Handler<RoutingContext> { routingContext ->
        routingContext.pathParams().get("maneuverId")?.let {
            val timedManeuver = routingContext.bodyAsJson
            timedManeuver.put("maneuverId", it)
            performRequest<JsonObject>(
                routingContext,
                PatientOperationIds.UPDATE_TIMED_MANEUVER,
                timedManeuver
            )
        } ?: RESTDefaultResponse.sendBadRequestResponse("Maneuver ID not specified", routingContext)
    }

    protected abstract fun <T> performRequest(routingContext: RoutingContext, busAdrr: String, message: Any)

    fun callBackMapping(): Map<String, Handler<RoutingContext>> {
        return mapOf(
            PatientOperationIds.GET_PATIENT to onGetPatient,
            PatientOperationIds.GET_MEDICAL_HISTORY to onGetMedicalHistory,
            PatientOperationIds.GET_ANAGRAPHIC to onGetAnagraphic,
            PatientOperationIds.GET_STATUS to onGetStatus,

            PatientOperationIds.GET_VITALPARAMETERS_HISTORY to onGetVitalParametersHistory,
            PatientOperationIds.GET_VITALPARAMETER to onGetCurrentVitalParameter,
            PatientOperationIds.GET_VITALPARAMETER_HISTORY to onGetVitalParameterHistory,
            PatientOperationIds.GET_VITALPARAMETERS to onGetCurrentVitalParameters,
            PatientOperationIds.ADD_VITALPARAMETERS to onAddVitalParameters,

            PatientOperationIds.UPDATE_MEDICAL_HISTORY to onUpdateMedicalHistory,
            PatientOperationIds.UPDATE_ANAGRAPHIC to onUpdateAnagraphic,
            PatientOperationIds.UPDATE_STATUS to onUpdateStatus,

            PatientOperationIds.ADD_ADMINISTRATION to onAddAdministration,
            PatientOperationIds.GET_ALL_ADMINISTRATION to onGetAdministrations,

            PatientOperationIds.GET_EXECUTED_MANEUVERS to onGetExecutedManeuvers,
            PatientOperationIds.ADD_MANEUVER to onAddManeuver,
            PatientOperationIds.GET_TIMED_MANEUVER to onGetTimedManeuver,
            PatientOperationIds.ADD_TIMED_MANEUVER to onAddTimedManeuver,
            PatientOperationIds.UPDATE_TIMED_MANEUVER to onUpdateTimedManeuver
        )
    }
}

