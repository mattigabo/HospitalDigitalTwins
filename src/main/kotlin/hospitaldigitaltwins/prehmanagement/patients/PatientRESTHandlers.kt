package hospitaldigitaltwins.prehmanagement.patients

import digitaltwinframework.coreimplementation.restmanagement.RESTDefaultResponse
import hospitaldigitaltwins.common.AbstractPatientRESTHandlers
import io.vertx.ext.web.RoutingContext

/**
 * Created by Matteo Gabellini on 05/03/2020.
 */
object PatientRESTHandlers : AbstractPatientRESTHandlers() {
    override fun <T> performRequest(routingContext: RoutingContext, busAdrr: String, message: Any) {
        routingContext.pathParams().get("missionId")?.let {

            eb.request<T>(busAdrr + it, message, responseCallBack(routingContext))

        } ?: RESTDefaultResponse.sendBadRequestResponse("MissionId not specified", routingContext)
    }
}