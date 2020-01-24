package digitaltwinframework

import java.net.InetAddress
import java.net.URI

/**
 * Created by Matteo Gabellini on 24/01/2020.
 */
interface IdentifierGenerator {
    fun nextIdentifier(): URI
}

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