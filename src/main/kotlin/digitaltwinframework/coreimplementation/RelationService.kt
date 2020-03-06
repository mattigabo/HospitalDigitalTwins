package digitaltwinframework.coreimplementation

import io.vertx.core.json.JsonObject

class RelationService {

    var relationToOtherDT: MutableMap<String, ArrayList<JsonObject>> = HashMap()
        private set

    fun addRelation(digitalTwinId: String, semantics: JsonObject) {
        if (this.relationToOtherDT.containsKey(digitalTwinId)) {
            this.relationToOtherDT.get(digitalTwinId)?.add(semantics)
        } else {
            var linkList = ArrayList<JsonObject>()
            linkList.add(semantics)
            this.relationToOtherDT.put(digitalTwinId, linkList)
        }
    }

    fun deleteRelation(digitalTwinId: String, semantics: JsonObject): Boolean {
        return this.relationToOtherDT.get(digitalTwinId)?.remove(semantics) ?: false
    }
}