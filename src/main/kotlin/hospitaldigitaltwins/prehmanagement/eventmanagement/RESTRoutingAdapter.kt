package hospitaldigitaltwins.prehmanagement.eventmanagement

import digitaltwinframework.coreimplementation.restmanagement.AbstractRESTInteractionAdapter

class RESTRoutingAdapter : AbstractRESTInteractionAdapter()

object EventOperationIDS {
    val POST_EVENT_INFO = "addEventInfo"
    val GET_EVENT_INFO = "getEventInfo"
}