package mapping

import javafx.scene.image.Image
import mapping.defines.StrokeColor
import mapping.objects.ZBufferString

class StringScan(val y: Int, val arr: List<ScanFacetInf>, val prevZBuff: Array<Array<Double>?>) {
    private val image = Image("/drawimage.jpg")
    public val width = image.width
    public val stringBuffer = ZBufferString(width.toInt())
    private var empY = true
    init {
        stringBuffer.fill()
        stringBuffer.eqbuffer = prevZBuff
    }

    public fun calcColorBuffer () : ZBufferString {
        if (arr.isNotEmpty()) {
            var emp = true
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
                if (arrI != -1) {//
                    val eqat = arr[arrI].getFacetEquation()
                    stringBuffer.zbuffer[x] = zi
                    if (!stringBuffer.eqbuffer[x].contentEquals(eqat)) {
                        stringBuffer.colorbuffer[x] = StrokeColor.color
                    }
                    else if (allR.contentEquals(eqat)) {
                        stringBuffer.colorbuffer[x] = arr[arrI].facet.color
                    }
                    else {
                        stringBuffer.colorbuffer[x] = StrokeColor.color
                        allR = eqat
                    }

                    // памятник моему поражению типизации Котлина
                    stringBuffer.eqbuffer[x]?.set(0, eqat[0]!!)
                    stringBuffer.eqbuffer[x]?.set(1, eqat[1]!!)
                    stringBuffer.eqbuffer[x]?.set(2, eqat[2]!!)
                    stringBuffer.eqbuffer[x]?.set(3, eqat[3]!!)

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
            empY = false
        }
//        else if (!empY) {
//            for (x in 0 until width.toInt()) {
//                if (!stringBuffer.eqbuffer.contentEquals(arrayOf(.0, .0, .0, .0))) {
//                    stringBuffer.colorbuffer[x] = StrokeColor.color
//                }
//                //empY = false
//            }
//        }
        return stringBuffer
    }
}