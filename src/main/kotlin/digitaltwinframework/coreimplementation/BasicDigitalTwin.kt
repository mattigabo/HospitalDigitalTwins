package digitaltwinframework.coreimplementation

import digitaltwinframework.DigitalTwin
import digitaltwinframework.LinkSemantic
import java.net.URI

/**
 * This class constitutes an abstract implementation of a Digital Twin
 * that provide basic implementation of the management of the core aspects
 *
 * */
abstract class BasicDigitalTwin(
        override val identifier: URI
) : DigitalTwin {

    var relationToOtherDT: MutableMap<URI, ArrayList<LinkSemantic>> = HashMap()

    override fun addLink(digitalTwinId: URI, semantic: LinkSemantic) {
        if (this.relationToOtherDT.containsKey(digitalTwinId)) {
            this.relationToOtherDT.get(digitalTwinId)?.add(semantic)
        } else {
            var linkList = ArrayList<LinkSemantic>()
            linkList.add(semantic)
            this.relationToOtherDT.put(digitalTwinId, linkList)
        }

    }

    override fun deleteLink(digitalTwinId: URI, semantic: LinkSemantic) {
        this.relationToOtherDT.get(digitalTwinId)?.remove(semantic)
    }
}