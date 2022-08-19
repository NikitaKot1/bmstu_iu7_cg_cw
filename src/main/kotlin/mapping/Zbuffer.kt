package mapping

import javafx.scene.image.WritableImage
import mapping.objects.SimpleObject

class Zbuffer (obj: SimpleObject, matr: TransformMatrix, image: WritableImage) {
    public var wimage = image
    public val newObj: SimpleObject = Transformator.transform(obj, matr)
    public val map = ListOfFacet.createMap(newObj)
    public var arr = emptyArray<ScanFacetInf>()
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
//            for (i in arr.indices) {
//                arr[i].dy--
//                if (arr[i].dy == 0)
//                    arr.drop(i)
//            }

        }
    }
}