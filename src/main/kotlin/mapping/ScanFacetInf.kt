package mapping

import mapping.objects.Facet
import kotlin.math.round

class ScanFacetInf (val facet: Facet){
    public var yScanMin = findYScanMin()
    public var dy = 0

    private fun findYScanMin () : Double{
        var yMax = facet.dotAr[0].yi
        yScanMin = facet.dotAr[0].yi
        for (y in facet.dotAr) {
            if (yScanMin > y.yi)
                yScanMin = y.yi
            if (yMax < y.yi)
                yMax = y.yi
        }
        dy = round(yMax - yScanMin).toInt()
        return yScanMin
    }
}