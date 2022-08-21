package mapping

import javafx.scene.image.Image
import mapping.defines.StrokeColor
import mapping.objects.ZBufferString

class StringScan(val y: Int, val arr: List<ScanFacetInf>) {
    private val image = Image("/drawimage.jpg")
    public val width = image.width
    public val stringBuffer = ZBufferString(width.toInt())
    init {
        stringBuffer.fill()
    }

    public fun calcColorBuffer () : ZBufferString {
        if (arr.isNotEmpty()) {
            var emp = true
            for (x in 0 until width.toInt()) {
                //first string
                var arrI1 = -1
                var zi1 = -Double.MAX_VALUE
                for (i in 0 until arr.count()) {
                    val z = arr[i].getZ(x, y)
                    if (z > zi1 && arr[i].area.contains(x.toDouble(), y.toDouble())) {
                        zi1 = z
                        arrI1 = i
                    }
                }
                //second string
                var arrI2 = -1
                var zi2 = -Double.MAX_VALUE
                for (i in 0 until arr.count()) {
                    val z = arr[i].getZ(x, y + 1)
                    if (z > zi2 && arr[i].area.contains(x.toDouble(), (y+1).toDouble())) {
                        zi2 = z
                        arrI2 = i
                    }
                }

                if (arrI1 != -1) {
                    stringBuffer.zbuffer[x] = zi1
                    if (arrI1 != arrI2) {
                        stringBuffer.colorbuffer[x] = StrokeColor.color
                    }
                    else {
                        stringBuffer.colorbuffer[x] = arr[arrI1].facet.color
                    }
                    if (emp) {
                        stringBuffer.colorbuffer[x] = StrokeColor.color
                        emp = false
                    }
                }
                else if (!emp) {
                    stringBuffer.colorbuffer[x] = StrokeColor.color
                    emp = true
                }
            }
        }
        return stringBuffer
    }
}