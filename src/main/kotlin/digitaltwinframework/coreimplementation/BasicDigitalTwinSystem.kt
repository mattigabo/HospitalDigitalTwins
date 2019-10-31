package digitaltwinframework.coreimplementation

import digitaltwinframework.DigitalTwinLink
import digitaltwinframework.DigitalTwinSystem
import digitaltwinframework.IdentifierGenerator
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

object ConfigUtils {

    fun projectPathUri(): String {
        return "file://${System.getProperty("user.dir")}"
    }

    fun resourceFolderPath(): String {
        return projectPathUri() + "/res/"
    }

    fun createUri(relativePath: String): String {
        return cleanSpace(resourceFolderPath() + relativePath)
    }

    fun cleanSpace(path: String): String {
        return path.replace(
            " ",
            "%20"
        )
    }
}