package hospitaldigitaltwins.prehmanagement.patients

import hospitaldigitaltwins.common.AbstractPatientRestHandlers
import hospitaldigitaltwins.prehmanagement.missions.MissionRestRequestForwarder
import io.vertx.ext.web.RoutingContext

/**
 * Created by Matteo Gabellini on 05/03/2020.
 */
object PatientRestHandlers : AbstractPatientRestHandlers() {
    override fun <T> performRequest(routingContext: RoutingContext, busAdrr: String, message: Any) {
        return MissionRestRequestForwarder.performRequest<T>(routingContext, busAdrr, message)
    }
}