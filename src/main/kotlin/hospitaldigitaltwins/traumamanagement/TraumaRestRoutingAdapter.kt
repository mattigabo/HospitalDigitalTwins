package hospitaldigitaltwins.traumamanagement

import digitaltwinframework.coreimplementation.restmanagement.AbstractRestInteractionAdapter
import digitaltwinframework.coreimplementation.utils.ConfigUtils
import hospitaldigitaltwins.traumamanagement.info.TraumaInfoOperationIds
import hospitaldigitaltwins.traumamanagement.info.TraumaInfoRestHandler
import hospitaldigitaltwins.traumamanagement.info.TraumaTeamOperationIds
import hospitaldigitaltwins.traumamanagement.info.TraumaTeamRestHandlers
import hospitaldigitaltwins.traumamanagement.patient.FinalDestinationOperationIds
import hospitaldigitaltwins.traumamanagement.patient.FinalDestinationRestHandler
import hospitaldigitaltwins.traumamanagement.patient.PatientRestHandlers
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.ext.web.RoutingContext

/**
 * Created by Matteo Gabellini on 05/03/2020.
 */
class TraumaRestRoutingAdapter(
    vertxInstance: Vertx,
    handlerServiceId: String
) : AbstractRestInteractionAdapter(vertxInstance, handlerServiceId) {

    override val adapterName: String
        get() = "TraumaDigitalTwinManagementApi"
    override val openApiSpecPath: String
        get() = ConfigUtils.createUri("hospital/TraumaApi-OpenApi-Schemas.yml")


    override fun operationCallbackMapping(): Map<String, Handler<RoutingContext>> {
        val partialCallbackMapping = mapOf(

            TraumaInfoOperationIds.GET_BASIC_INFO to TraumaInfoRestHandler.onGetInfoService,

            //LocationOperationIds.GET_LOCATIONS to LocationRestHandlers.onGetLocations,
            //LocationOperationIds.ENTRY_IN_LOCATION to LocationRestHandlers.onEntryInLocation,
            //LocationOperationIds.EXIT_FROM_LOCATION to LocationRestHandlers.onExitFromLocation,


            TraumaTeamOperationIds.ACTIVATE_TRAUMA_TEAM to TraumaTeamRestHandlers.onActivateTraumaTeam,
            TraumaTeamOperationIds.GET_ACTIVATION_INFO to TraumaTeamRestHandlers.onGetActivationInfo,
            TraumaTeamOperationIds.TAKE_PATIENT_IN_CHARGE to TraumaTeamRestHandlers.onTakePatientInCharge,

            FinalDestinationOperationIds.GET_FINAL_DESTINATION to FinalDestinationRestHandler.onGetFinalDestination,
            FinalDestinationOperationIds.SET_FINAL_DESTINATION to FinalDestinationRestHandler.onSetFinalDestination
        )
        val result = HashMap(PatientRestHandlers.callBackMapping())
        result.putAll(partialCallbackMapping)
        return result
    }
}