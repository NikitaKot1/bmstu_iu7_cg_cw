package mapping

import javafx.scene.image.Image
import mapping.objects.SimpleObject

class StringScan (obj: SimpleObject, matr: RotateMatrix) {
    private val image = Image("/drawimage.jpg")
    public val width = image.width
    public val newObj: SimpleObject = Transformator.transform(obj, matr)
    public val map = ListOfFacet.createMap(obj)
        
}