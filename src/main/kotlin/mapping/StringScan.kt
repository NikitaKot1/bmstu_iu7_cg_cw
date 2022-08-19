package mapping

import javafx.scene.image.Image
import mapping.defines.StrokeColor
import mapping.objects.ZBufferString
import java.util.DoubleSummaryStatistics

class StringScan (val y: Int, val arr: Array<ScanFacetInf>) {
    private val image = Image("/drawimage.jpg")
    public val width = image.width

    public val stringBuffer = ZBufferString(width.toInt())
    init {
        stringBuffer.fill()
    }

    public fun calcColorBuffer () : ZBufferString {
        if (arr.isNotEmpty()) {
            var allR = arr[0].getFacetEquation()
            for (x in 0 until width.toInt()) {
                var arrI = -1
                var zi = -Double.MAX_VALUE
                for (i in 0 until arr.count()) {
                    val z = arr[i].getZ(x, y)
                    if (z > zi && arr[i].area.contains(x.toDouble(), y.toDouble())) {
                        zi = z
                        arrI = i
                    }
                }
                if (arrI != -1) {
                    stringBuffer.zbuffer[x] = zi
                    if (allR.contentEquals(arr[arrI].getFacetEquation()))
                        stringBuffer.colorbuffer[x] = arr[arrI].facet.color
                    else {
                        stringBuffer.colorbuffer[x] = StrokeColor.color
                        allR = arr[arrI].getFacetEquation()
                    }
                }
            }
        }
        return stringBuffer
    }
}