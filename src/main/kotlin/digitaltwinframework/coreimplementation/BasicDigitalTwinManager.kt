package digitaltwinframework.coreimplementation

import digitaltwinframework.*
import java.net.InetAddress
import java.net.URI

/*
*   This is a basic implementation of a digital twin identifier generator. It is usable
*   from a Digital Twin Manager implementation in order to generate new unique identifier for digital twins instances
*   Basic Identifier schema: "dt://machine-ip/dt-realm-name/progressiveId"
* */
class BasicIdentifierGenerator : IdentifierGenerator {
    private var idCounter = 0
    private var dtRealmName = "BasicDigitalTwinRealm"

    override fun nextIdentifier(): URI {
        val address = InetAddress.getLocalHost()
        val ip = address.hostAddress
        return URI("dt://${ip}/${dtRealmName}/${idCounter++}")
    }
}

class BasicDigitalTwinManager : DigitalTwinManager {

    var localLinkStorage: ArrayList<DigitalTwinLink> = ArrayList()
    var restServer: RESTServer
    val identifierGenerator = BasicIdentifierGenerator()

    init {
        println("Digital Twin Manager ---> Creating REST server")
        restServer = RESTServer.getInstance()
    }

    override fun createDigitalTwin(factory: DigitalTwinFactory): URI {
        var dt = factory.create(identifierGenerator.nextIdentifier())
        return dt.identifier
    }

    override fun killDigitalTwin(target: URI) {

    }

    override fun enstablishLink(firstDT: DigitalTwinMetaInfo, secondDT: DigitalTwinMetaInfo, semantic: LinkSemantic) {

    }

    override fun deleteLingk(link: DigitalTwinLink) {

    }
}