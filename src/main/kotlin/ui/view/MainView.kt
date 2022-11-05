package ui.view

import javafx.geometry.Pos
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import mapping.fileload.FileManager
import mapping.math.Vector3
import mapping.rendering.Render
import tornadofx.*

class MainView : View("MainWindow") {
    private val image = Image("/drawimage.jpg")
    private val wimage = WritableImage(image.pixelReader, image.width.toInt(), image.height.toInt())
    val render = Render(wimage)
    val fileMan = FileManager()
    var scene1 = fileMan.loadScene("/home/zorox/Документы/bmstu_iu7_cg_cw/src/main/resources/monkey.sol", 200.0)

    private fun torad(rad: Double) :Double {
        return rad / 180 * 3.14
    }

    override val root = hbox {
        imageview(wimage).apply {
            var cenw = wimage.width / 2
            val cenh = wimage.height / 2
            if (cenw > cenh)
                cenw = cenh
            cenw /= 2

            scene1.models[0].transform(Vector3(0.0, 0.0, 0.0), Vector3(cenw, cenw, cenw), Vector3(0.0, 0.0, 0.0))

            println(wimage.width)
            println(wimage.height)
            scene1.models[0].transform(Vector3(200.0, 200.0, 1000.0), Vector3(1.0, 1.0, 1.0), Vector3(0.0, 0.0, 0.0))
            render.renderScene(scene1)
        }
        hboxConstraints {
            prefWidth = 1000.0
            prefHeight = 1000.0
        }
        vbox {
            alignment = Pos.CENTER
            spacing = 10.0
            run {
                label { text = "Верчение" }
                button {
                    text = "z : up"
                    action {
                        val k = torad(10.0)
                        scene1.models[0].transform(Vector3(0.0, 0.0, 0.0), Vector3(1.0, 1.0, 1.0), Vector3(0.0, 0.0, k))
                        render.renderScene(scene1)
                    }
                }
                hbox {
                    alignment = Pos.CENTER
                    spacing = 10.0
                    button {
                        text = "left"
                        action {
                            val k = torad(10.0)
                            scene1.models[0].transform(Vector3(0.0, 0.0, 0.0), Vector3(1.0, 1.0, 1.0), Vector3(0.0, -k, 0.0))
                            render.renderScene(scene1)
                        }
                    }
                    button {
                        text = "right"
                        action {
                            val k = torad(10.0)
                            scene1.models[0].transform(Vector3(0.0, 0.0, 0.0), Vector3(1.0, 1.0, 1.0), Vector3(0.0, k, 0.0))
                            render.renderScene(scene1)
                        }
                    }
                }
                button {
                    text = "z : down"
                    action {
                        val k = torad(10.0)
                        scene1.models[0].transform(Vector3(0.0, 0.0, 0.0), Vector3(1.0, 1.0, 1.0), Vector3(0.0, 0.0, -k))
                        render.renderScene(scene1)
                    }
                }
            }

        }
        button {
            text = "right"
            action {
                scene1.models[0].transform(Vector3(10.0, 0.0, 0.0), Vector3(1.0, 1.0, 1.0), Vector3(0.0, 0.0, 0.0))
                render.renderScene(scene1)
            }
        }
        button {
            text = "down"
            action {
                scene1.models[0].transform(Vector3(0.0, 10.0, 0.0), Vector3(1.0, 1.0, 1.0), Vector3(0.0, 0.0, 0.0))
                render.renderScene(scene1)
            }
        }
    }
}