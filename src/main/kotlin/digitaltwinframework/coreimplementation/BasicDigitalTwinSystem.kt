package digitaltwinframework.coreimplementation

/*class BasicDigitalTwinSystem private constructor() : DigitalTwinSystem {

    override val name = "BasicDigitalTwinSystem"

    var localRelationStorage: ArrayList<DigitalTwinRelation> = ArrayList()
    val identifierGenerator = BasicIdentifierGenerator()
    var vertx = Vertx.vertx()
    var eventBus = vertx.eventBus()

    var restServer: RESTServer
    var runningDT = ArrayList<URI>()

    private var infoRequestHandler = { routingContext: RoutingContext ->
        val response = routingContext.response()
        response.putHeader("content-type", "text/json")

        val responseBody = """
        {
            "digitalTwinsystem":{
            "name":${this.dtSystem.name}
            "RunningDigitalTwin": ${this.dtSystem.runningDT.size}
        }
        }
        """.trimIndent()
        response.end(responseBody)
    }


    companion object {
        var RUNNING_INSTANCE: BasicDigitalTwinSystem? = null
            private set

        @JvmStatic
        fun boot(): BasicDigitalTwinSystem {
            RUNNING_INSTANCE?.let {
                return RUNNING_INSTANCE as BasicDigitalTwinSystem
            }

            return BasicDigitalTwinSystem()
        }
    }

    init {
        BasicDigitalTwinSystem.RUNNING_INSTANCE = this
        restServer = RESTServer(this)
        vertx.deployVerticle(restServer)
    }

    override fun RESTServerInstance(): RESTServer {
        return restServer
    }

    override fun shutdown() {
        this.runningDT.forEach { it.value.stop() }
        vertx.close()
        BasicDigitalTwinSystem.RUNNING_INSTANCE = null
    }
}*/

