package hospitaldigitaltwins.prehmanagement.eventmanagement

import digitaltwinframework.coreimplementation.restmanagement.AbstractRESTInteractionAdapter
import digitaltwinframework.coreimplementation.utils.ConfigUtils
import hospitaldigitaltwins.prehmanagement.missions.MissionOperationIds
import hospitaldigitaltwins.prehmanagement.missions.MissionRestHandlers
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.ext.web.RoutingContext

/**
 * Created by Matteo Gabellini on 06/02/2020.
 */
class RESTRoutingAdapter(
    vertxInstance: Vertx,
    handlerServiceId: String
) : AbstractRESTInteractionAdapter(vertxInstance, handlerServiceId) {

    override val adapterName: String
        get() = "PreHDigitalTwinManagementApi"
    override val openApiSpecPath: String
        get() = ConfigUtils.createUri("hospital/PreHApi-OpenApi-Schemas.yml")


    override fun operationCallbackMapping(): Map<String, Handler<RoutingContext>> {
        return mapOf(
            EventOperationIds.GET_EVENT_INFO to EventRestHandlers.onAddEventInfo,
            EventOperationIds.ADD_EVENT_INFO to EventRestHandlers.onAddEventInfo,
            EventOperationIds.GET_MISSIONS to EventRestHandlers.onGetMissions,
            EventOperationIds.ADD_MISSION to EventRestHandlers.onAddMission,

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
    }
}