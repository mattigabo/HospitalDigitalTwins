package digitaltwinframework.coreimplementation

import io.vertx.core.json.JsonObject
import java.net.URI

class RelationService {

    var relationToOtherDT: MutableMap<URI, ArrayList<JsonObject>> = HashMap()
        private set

    fun addRelation(digitalTwinId: URI, relation: JsonObject) {
        if (this.relationToOtherDT.containsKey(digitalTwinId)) {
            this.relationToOtherDT.get(digitalTwinId)?.add(relation)
        } else {
            var linkList = ArrayList<JsonObject>()
            linkList.add(relation)
            this.relationToOtherDT.put(digitalTwinId, linkList)
        }
    }

    fun deleteRelation(digitalTwinId: URI, relation: JsonObject): Boolean {
        return this.relationToOtherDT.get(digitalTwinId)?.remove(relation) ?: false
    }
}