package mapping

import javafx.scene.image.WritableImage
import mapping.math.matrixes.MoveMatrix
import mapping.objects.SimpleObject
import mapping.math.matrixes.TransformMatrix

class Zbuffer (obj: SimpleObject, matr: TransformMatrix, image: WritableImage) {
    public var wimage = image
    public val newObj: SimpleObject = Transformator.transform(obj, matr)
    public val map = ListOfFacet.createMap(newObj)
    public var arr = mutableListOf<ScanFacetInf>()
    var eqbuffer = arrayOfNulls<Array<Double>>(wimage.width.toInt())
    init {
        for (i in eqbuffer.indices) {
            val arr = arrayOf(.0, .0, .0, .0)
            eqbuffer[i] = arr
        }
    }

    public fun drawScene (){
        for (y in 0 until wimage.height.toInt()) {
            if (map.containsKey(y)) {
                arr += map[y]!!
            }
            val zbuff = StringScan(y, arr, eqbuffer).calcColorBuffer()
            val cbuffer = zbuff.colorbuffer
            eqbuffer = zbuff.eqbuffer
            val pW = wimage.pixelWriter
            for (x in 0 until wimage.width.toInt()) {
                pW.setColor(x, y, cbuffer[x])
            }
            for (i in arr.size - 1 downTo 0) {
                arr[i].dy--
                if (arr[i].dy == 0) {
                    arr.removeAt(i)

                }
            }

        }
        val ma = MoveMatrix(.0, .0, .0)
    }
}