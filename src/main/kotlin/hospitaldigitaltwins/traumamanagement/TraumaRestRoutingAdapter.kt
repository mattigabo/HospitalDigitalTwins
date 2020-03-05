package hospitaldigitaltwins.traumamanagement

import digitaltwinframework.coreimplementation.restmanagement.AbstractRestInteractionAdapter
import digitaltwinframework.coreimplementation.utils.ConfigUtils
import hospitaldigitaltwins.prehmanagement.missions.MissionOperationIds
import hospitaldigitaltwins.prehmanagement.missions.MissionRestHandlers
import hospitaldigitaltwins.prehmanagement.patients.PatientRestHandlers
import hospitaldigitaltwins.traumamanagement.location.LocationOperationIds
import hospitaldigitaltwins.traumamanagement.location.LocationRestHandlers
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
            LocationOperationIds.GET_LOCATIONS to LocationRestHandlers.onGetLocations,
            LocationOperationIds.ENTRY_IN_LOCATION to LocationRestHandlers.onEntryInLocation,
            LocationOperationIds.EXIT_FROM_LOCATION to LocationRestHandlers.onExitFromLocation,

            MissionOperationIds.GET_MISSION to MissionRestHandlers.onInfoRequest,
            MissionOperationIds.PUT_MEDIC to MissionRestHandlers.onMedicUpdate,
            MissionOperationIds.GET_MEDIC to MissionRestHandlers.onMedicRequest,

            MissionOperationIds.PUT_RETURN_INFO to MissionRestHandlers.onReturnInfoUpdate,
            MissionOperationIds.GET_RETURN_INFO to MissionRestHandlers.onReturnInfoRequest,

            MissionOperationIds.GET_TRACKING to MissionRestHandlers.onTrackingInfoRequest,
            MissionOperationIds.DEPARTURE_FROM_HOSPITAL to MissionRestHandlers.onDepartureFromHostpital,
            MissionOperationIds.ARRIVAL_ON_SITE to MissionRestHandlers.onArrivalOnSite,
            MissionOperationIds.DEPARTURE_FROM_SITE to MissionRestHandlers.onDepartureFromSite,
            MissionOperationIds.ARRIVAL_AT_THE_HOSPITAL to MissionRestHandlers.onArrivalAtHostpital
        )
        val result = HashMap(PatientRestHandlers.callBackMapping())
        result.putAll(partialCallbackMapping)
        return result
    }
}