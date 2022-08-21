package mapping

import javafx.scene.image.WritableImage
import mapping.objects.SimpleObject
import mapping.transform.TransformMatrix
import mapping.transform.Transformator
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Zbuffer (obj: SimpleObject, matr: TransformMatrix, image: WritableImage) {
    public var wimage = image
    public val newObj: SimpleObject = Transformator.transform(obj, matr)
    public val map = ListOfFacet.createMap(newObj)

    public fun drawScene () {
        val myService:ExecutorService = Executors.newFixedThreadPool(8)
        for (y in 0 until wimage.height.toInt()) {
            myService.submit {
                //create map
                val arr = mutableListOf<ScanFacetInf>()
                for (yo in 0 until y + 2) {
                    if (map.containsKey(yo))
                        arr += map[yo]!!
                }
                if (arr.size > 0) {
                    for (el in arr.size - 1 downTo 0) {
                        val nowdy = arr[el].dy + arr[el].yScanMin - y
                        if (nowdy < 0)
                            arr.removeAt(el)
                    }
                }

                val zbuff = StringScan(y, arr).calcColorBuffer()
                val cbuffer = zbuff.colorbuffer
                val pW = wimage.pixelWriter
                for (x in 0 until wimage.width.toInt()) {
                    pW.setColor(x, y, cbuffer[x])
                }
            }
        }
    }
}