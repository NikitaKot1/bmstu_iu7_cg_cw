package ui.view

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import mapping.fileload.FileManager
import mapping.math.Vector3
import mapping.rendering.Render
import mapping.scene.Scene
import tornadofx.*

class MainView : View("MainWindow") {
    private val image = Image("/drawimage.jpg")
    private val wimage = WritableImage(image.pixelReader, image.width.toInt(), image.height.toInt())
    val render = Render(wimage)
    val fileMan = FileManager()
    var scene : mapping.scene.Scene? = null
    override val root = hbox {
        imageview(wimage).apply {
            var cenw = wimage.width / 2
            val cenh = wimage.height / 2
            if (cenw > cenh)
                cenw = cenh
            cenw /= 2
            val scene1 = fileMan.loadScene("/home/zorox/Документы/bmstu_iu7_cg_cw/src/main/resources/monkey.sol", cenw)

            println(scene1.camera.pos.position.z)
            render.renderScene(scene1)
            //for (i in 0..360) {
                scene1.models[0].transform(Vector3(0.0, 0.0, 0.0), Vector3(1.0, 1.0, 1.0), Vector3(90.0, 90.0, 1.0))
                render.renderScene(scene1)
            //}

        }
        hboxConstraints {
            prefWidth = 1000.0
            prefHeight = 1000.0
        }

        button {
            text = "->"
            action {

            }
        }
    }
}