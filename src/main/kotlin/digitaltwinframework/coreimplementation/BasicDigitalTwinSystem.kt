package digitaltwinframework.coreimplementation

import digitaltwinframework.BasicIdentifierGenerator
import digitaltwinframework.DigitalTwinRelation
import digitaltwinframework.DigitalTwinSystem
import io.vertx.core.Vertx
import java.net.URI

class BasicDigitalTwinSystem private constructor() : DigitalTwinSystem {

    override val name = "BasicDigitalTwinSystem"

    var localRelationStorage: ArrayList<DigitalTwinRelation> = ArrayList()
    var restServer: RESTServer
    val identifierGenerator = BasicIdentifierGenerator()
    var vertx = Vertx.vertx()
    var eventBus = vertx.eventBus()

    var runningDT = ArrayList<URI>()

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
}

