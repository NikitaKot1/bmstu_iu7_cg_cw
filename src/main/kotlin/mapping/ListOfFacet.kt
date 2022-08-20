package mapping

import mapping.objects.SimpleObject

object ListOfFacet{
    public fun createMap(obj: SimpleObject) : Map<Int, List<ScanFacetInf>>{
        val map = mutableMapOf<Int, List<ScanFacetInf>>()
        for (facet in obj.facets) {
            val inf = ScanFacetInf(facet)
            if (map.containsKey(inf.yScanMin.toInt())) {
                val arr = map[inf.yScanMin.toInt()]
                map[inf.yScanMin.toInt()] = arr!! + listOf(inf)
            } else {
                map[inf.yScanMin.toInt()] = listOf(inf)
            }
        }
        return map
    }
}