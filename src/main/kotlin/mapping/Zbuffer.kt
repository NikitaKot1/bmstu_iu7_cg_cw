package mapping

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import mapping.objects.SimpleObject

class Zbuffer (obj: SimpleObject, matr: RotateMatrix) {
    //private val objs = emptyArray<SimpleObject>()
    private val image = Image("/drawimage.jpg")
    private val wimage = WritableImage(image.pixelReader, image.width.toInt(), image.height.toInt())
    public val newObj: SimpleObject = Transformator.transform(obj, matr)
    public val map = ListOfFacet.createMap(newObj)
    public var arr = emptyArray<ScanFacetInf>()

    public fun drawScene (){
        for (y in 0..image.height.toInt()) {
            if (map.containsKey(y)) {
                arr += map[y]!!
            }
            val cbuffer = StringScan(y, arr).calcColorBuffer().colorbuffer
            val pW = wimage.pixelWriter
            for (x in 0 until image.width.toInt()) {
                pW.setColor(x, y, cbuffer[x])
            }
        }
    }
}