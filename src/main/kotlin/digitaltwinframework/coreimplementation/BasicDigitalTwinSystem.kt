package digitaltwinframework.coreimplementation

import digitaltwinframework.*
import io.vertx.core.Vertx
import java.net.InetAddress
import java.net.URI

/*
*   This is a basic implementation of a digital twin identifier generator. It is usable
*   from a Digital Twin Manager implementation in order to generate new unique identifier for digital twins instances
*   Basic Identifier schema: "DT.machine_ip.realm_name.progressiveId"
* */
class BasicIdentifierGenerator : IdentifierGenerator {
    private var idCounter = 0
    private var dtRealmName = "BasicDigitalTwinRealm"

    override fun nextIdentifier(): URI {
        val address = InetAddress.getLocalHost()
        val ip = address.hostAddress
        return URI("${idCounter++}")//"DT.${ip}.${dtRealmName}.${idCounter++}")
    }
}

class BasicDigitalTwinSystem private constructor() : DigitalTwinSystem {

    override val name = "BasicDigitalTwinSystem"

    var localLinkStorage: ArrayList<DigitalTwinLink> = ArrayList()
    var restServer: RESTServer
    val identifierGenerator = BasicIdentifierGenerator()
    var vertx = Vertx.vertx()
    var eventBus = vertx.eventBus()

    var runningDT = HashMap<URI, DigitalTwin>()

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

    override fun createDigitalTwin(factory: DigitalTwinFactory): URI {
        var dt = factory.create(identifierGenerator.nextIdentifier())
        this.runningDT.put(dt.identifier, dt)
        return dt.identifier
    }

    override fun killDigitalTwin(target: URI) {

    }

    override fun enstablishLink(firstDT: DigitalTwinMetaInfo, secondDT: DigitalTwinMetaInfo, semantic: LinkSemantic) {

    }

    override fun deleteLingk(link: DigitalTwinLink) {

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
