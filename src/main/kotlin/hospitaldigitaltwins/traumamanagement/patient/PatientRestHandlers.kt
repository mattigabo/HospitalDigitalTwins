package hospitaldigitaltwins.traumamanagement.patient

import hospitaldigitaltwins.common.AbstractPatientRestHandlers
import io.vertx.ext.web.RoutingContext

/**
 * Created by Matteo Gabellini on 05/03/2020.
 */
class PatientRestHandlers : AbstractPatientRestHandlers() {
    override fun <T> performRequest(routingContext: RoutingContext, busAdrr: String, message: Any) {
        eb.request<T>(busAdrr, message, responseCallBack(routingContext))
    }
}