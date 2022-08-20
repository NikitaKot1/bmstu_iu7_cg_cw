package mapping.objects

open class SimpleObject {
    public var facets = mutableListOf<Facet>()

    public fun clone() : SimpleObject {
        val newObj = SimpleObject()
        for (x in this.facets) {
            newObj.facets += x.clone()
        }
        return newObj
    }
}