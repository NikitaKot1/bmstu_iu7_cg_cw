package mapping

import javafx.scene.image.Image
import mapping.objects.SimpleObject

class ListOfFacet (private val obj: SimpleObject){
    private val image = Image("/drawimage.jpg")
    public val height = image.height
    public var map = mutableMapOf<Int, Array<ScanFacetInf>>()
    init {
        createMap()
    }
    public fun createMap() {
        map = mutableMapOf<Int, Array<ScanFacetInf>>()
        for (facet in obj.facets) {
            val inf = ScanFacetInf(facet)
            if (map.containsKey(inf.yScanMin.toInt())) {
                var arr = map[inf.yScanMin.toInt()]
                map[inf.yScanMin.toInt()] = arr!! + arrayOf(inf)
            } else {
                map[inf.yScanMin.toInt()] = arrayOf(inf)
            }
        }
    }
}