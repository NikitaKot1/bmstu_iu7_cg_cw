package mapping

import javafx.scene.image.Image
import mapping.objects.SimpleObject
import mapping.objects.ZBufferString

class StringScan (val y: Int) {
    private val image = Image("/drawimage.jpg")
    public val width = image.width

    public val stringBuffer = ZBufferString(width.toInt())
    init {
        stringBuffer.fill()
    }


        
}