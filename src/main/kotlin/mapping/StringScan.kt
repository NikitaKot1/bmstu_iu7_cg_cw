package mapping

import javafx.scene.image.Image
import mapping.objects.ZBufferString

class StringScan (val y: Int, val arr: Array<ScanFacetInf>) {
    private val image = Image("/drawimage.jpg")
    public val width = image.width

    public val stringBuffer = ZBufferString(width.toInt())
    init {
        stringBuffer.fill()
    }

    public fun calcColorBuffer () : ZBufferString{
        for (x in 0 until width.toInt()) {
            for (area in arr) {
                val z = area.getZ(x, y)
                if (area.area.contains(x.toDouble(), y.toDouble()) && (stringBuffer.zbuffer[x]!! > z)) {
                    stringBuffer.zbuffer[x] = z
                    stringBuffer.colorbuffer[x] = area.facet.color
                }
            }
        }
        return stringBuffer
    }
}