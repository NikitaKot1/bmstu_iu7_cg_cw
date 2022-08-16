package mapping

import mapping.objects.SimpleObject

object ListOfFacet{
    public fun createMap(obj: SimpleObject) : Map<Int, Array<ScanFacetInf>>{
        val map = mutableMapOf<Int, Array<ScanFacetInf>>()
        for (facet in obj.facets) {
            val inf = ScanFacetInf(facet)
            if (map.containsKey(inf.yScanMin.toInt())) {
                val arr = map[inf.yScanMin.toInt()]
                map[inf.yScanMin.toInt()] = arr!! + arrayOf(inf)
            } else {
                map[inf.yScanMin.toInt()] = arrayOf(inf)
            }
        }
        return map
    }
}