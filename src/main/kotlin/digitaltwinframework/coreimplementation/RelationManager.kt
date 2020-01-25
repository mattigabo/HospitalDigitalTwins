package digitaltwinframework.coreimplementation

import java.net.URI

class RelationManager {

    var relationToOtherDT: MutableMap<URI, ArrayList<Semantics>> = HashMap()

    fun addRelation(digitalTwinId: URI, semantic: Semantics) {
        if (this.relationToOtherDT.containsKey(digitalTwinId)) {
            this.relationToOtherDT.get(digitalTwinId)?.add(semantic)
        } else {
            var linkList = ArrayList<Semantics>()
            linkList.add(semantic)
            this.relationToOtherDT.put(digitalTwinId, linkList)
        }
    }

    fun deleteRelation(digitalTwinId: URI, semantic: Semantics): Boolean {
        return this.relationToOtherDT.get(digitalTwinId)?.remove(semantic) ?: false
    }
}