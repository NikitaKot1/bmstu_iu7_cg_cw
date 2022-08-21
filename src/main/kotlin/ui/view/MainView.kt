package ui.view

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import mapping.math.matrixes.TransformMatrix
import mapping.Zbuffer
import mapping.objects.Dot
import mapping.objects.examples.Cube
import tornadofx.*

class MainView : View("MainWindow") {
    private val image = Image("/drawimage.jpg")
    private val wimage = WritableImage(image.pixelReader, image.width.toInt(), image.height.toInt())
    val matr = TransformMatrix()
    val obj = Cube()
    override val root = hbox {
        imageview(wimage).apply {
            matr.rotate(Dot(.0, 90.0, 1.0))
            matr.move(Dot(100.0, 100.0, .0))
            //matr.scale(Dot(1.0, 2.0, 1.0))
            Zbuffer(obj, matr, wimage).drawScene()
        }
        hboxConstraints {
            prefWidth = 1000.0
            prefHeight = 1000.0
        }

        button {
            text = "->"
            action {
                matr.move(Dot(100.0, 0.0, .0))
                Zbuffer(obj, matr, wimage).drawScene()
            }
        }
    }
}