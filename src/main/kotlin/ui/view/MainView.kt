package ui.view

import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.image.WritableImage
import mapping.transform.TransformMatrix
import mapping.Zbuffer
import mapping.objects.Dot
import mapping.objects.examples.Cube
import tornadofx.*

class MainView : View("MainWindow") {
    private val image = Image("/drawimage.jpg")
    private val wimage = WritableImage(image.pixelReader, image.width.toInt(), image.height.toInt())

    override val root = hbox {
        imageview(wimage).apply {
            //fillGood(wimage)
            val matr = TransformMatrix()
            matr.rotate(Dot(.0, 90.0, 1.0))
            matr.move(Dot(100.0, 100.0, .0))
            //matr.scale(Dot(1.0, 2.0, 1.0))
            val obj = Cube()
            Zbuffer(obj, matr, wimage).drawScene()
        }
        hboxConstraints {
            prefWidth = 1000.0
            prefHeight = 1000.0
        }
    }

    private fun fillGood (image: WritableImage){
        val pr = image.pixelReader
        val pW = image.pixelWriter
        for (x in 0 until image.width.toInt())
            for (y in 0 until image.height.toInt()){
                val color = Color.rgb((x % 255), (y % 255), (x * y % 255))
                pW.setColor(x, y, color)
            }
    }
}