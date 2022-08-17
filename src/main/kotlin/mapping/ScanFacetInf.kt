package mapping

import mapping.objects.Facet
import java.awt.Shape
import java.awt.geom.Area
import java.awt.geom.GeneralPath
import kotlin.math.round

class ScanFacetInf (val facet: Facet){
    public var yScanMin = findYScanMin()
    public var dy = 0
    public var area = Area()
    public val path = GeneralPath()
    init {
        path.moveTo(facet.dots[0].xi, facet.dots[0].yi)
        for (i in 0..facet.dots.size) {
            path.lineTo(facet.dots[i].xi, facet.dots[i].yi)
        }
        path.closePath()
        area = Area(path)
    }

    public fun findYScanMin () : Double{
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

    public fun getFacetEquation () : Array<Double?> {
        val x0: Double = facet.dots[0].xi
        val y0: Double = facet.dots[0].yi
        val z0: Double = facet.dots[0].zi

        val x1: Double = facet.dots[1].xi
        val y1: Double = facet.dots[1].yi
        val z1: Double = facet.dots[1].zi

        val x2: Double = facet.dots[2].xi
        val y2: Double = facet.dots[2].yi
        val z2: Double = facet.dots[2].zi

        val a: Double = (y1 - y0) * (z2 - z0) - (y2 - y0) * (z1 - z0)
        val b: Double = (z1 - z0) * (x2 - x0) - (z2 - z0) * (x1 - x0)
        val c: Double = (x1 - x0) * (y2 - y0) - (x2 - x0) * (y1 - y0)
        val d: Double = -1 * (a * x0 + b * y0 + c * z0)

        val equation = arrayOfNulls<Double>(4)
        equation[0] = a
        equation[1] = b
        equation[2] = c
        equation[3] = d
        return equation
    }
}