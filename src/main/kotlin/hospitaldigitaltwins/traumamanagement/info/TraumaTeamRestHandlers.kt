package hospitaldigitaltwins.traumamanagement.info

import digitaltwinframework.coreimplementation.restmanagement.EventBusRestRequestForwarder
import digitaltwinframework.coreimplementation.utils.eventbusutils.StandardMessages
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext

/**
 * Created by Matteo Gabellini on 05/03/2020.
 */
object TraumaTeamRestHandlers {
    val onActivateTraumaTeam = Handler<RoutingContext> { routingContext ->
        val traumaTeamInfo = routingContext.bodyAsJson
        EventBusRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            TraumaTeamOperationIds.ACTIVATE_TRAUMA_TEAM,
            traumaTeamInfo
        )
    }

    val onGetActivationInfo = Handler<RoutingContext> { routingContext ->
        EventBusRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            TraumaTeamOperationIds.GET_ACTIVATION_INFO,
            StandardMessages.EMPTY_MESSAGE
        )
    }

    val onTakePatientInCharge = Handler<RoutingContext> { routingContext ->
        val psCode = routingContext.bodyAsJson
        EventBusRestRequestForwarder.performRequest<JsonObject>(
            routingContext,
            TraumaTeamOperationIds.TAKE_PATIENT_IN_CHARGE,
            psCode
        )
    }
}

object TraumaTeamOperationIds {
    const val ACTIVATE_TRAUMA_TEAM = "activateTraumaTeam"
    const val GET_ACTIVATION_INFO = "getActivationInfo"
    const val TAKE_PATIENT_IN_CHARGE = "takePatientInCharge"
}