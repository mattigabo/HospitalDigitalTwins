package digitaltwinframework.coreimplementation

import io.vertx.core.json.JsonObject
import java.net.URI

class RelationService {

    var relationToOtherDT: MutableMap<URI, ArrayList<JsonObject>> = HashMap()
        private set

    fun addRelation(digitalTwinId: URI, semantics: JsonObject) {
        if (this.relationToOtherDT.containsKey(digitalTwinId)) {
            this.relationToOtherDT.get(digitalTwinId)?.add(semantics)
        } else {
            var linkList = ArrayList<JsonObject>()
            linkList.add(semantics)
            this.relationToOtherDT.put(digitalTwinId, linkList)
        }
    }

    fun deleteRelation(digitalTwinId: URI, semantics: JsonObject): Boolean {
        return this.relationToOtherDT.get(digitalTwinId)?.remove(semantics) ?: false
    }
}