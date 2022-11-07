package ui.view

import javafx.beans.property.SimpleBooleanProperty
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
    val visible = Array(3) {true}
    val transform_flags = Array(2) {false}

    var pressedX = 0.0
    var pressedY = 0.0

    private fun torad(rad: Double) :Double {
        return rad / 180 * 3.14
    }

    override val root = hbox {
        imageview(wimage).apply {

            this.setOnMousePressed {
                pressedX = it.x
                pressedY = it.y
            }

            this.setOnMouseDragged {
                val dx = it.x - pressedX
                val dy = it.y - pressedY
                pressedY = it.y
                pressedX = it.x

                if (transform_flags[0]) {
                    val move_params = Vector3(dx, - dy, 0.0)
                    scene1.models[0].transform(move_params, Vector3(1.0, 1.0, 1.0), Vector3(0.0, 0.0, 0.0))
                    render.renderScene(scene1, visible)
                }
                else if (transform_flags[1]) {
                    val rotate_params = Vector3(-torad(dy) * 0.1, -torad(dx) * 0.1, 0.0)
                    scene1.models[0].transform(Vector3(0.0, 0.0, 0.0), Vector3(1.0, 1.0, 1.0), rotate_params)
                    render.renderScene(scene1, visible)
                }
            }



            var cenw = wimage.width / 2
            val cenh = wimage.height / 2
            if (cenw > cenh)
                cenw = cenh
            cenw /= 2



            scene1.models[0].transform(Vector3(cenw * 2, cenw * 2, .0), Vector3(cenw, cenw, cenw), Vector3(0.0, 0.0, 0.0))
            scene1.models[0].poligons.setArithCenter()
            render.renderScene(scene1, visible)
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
                    text = "up"
                    action {
                        val k = torad(10.0)
                        scene1.models[0].transform(Vector3(0.0, 0.0, 0.0), Vector3(1.0, 1.0, 1.0), Vector3(k, 0.0, 0.0))
                        render.renderScene(scene1, visible)
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
                            render.renderScene(scene1, visible)
                        }
                    }
                    button {
                        text = "right"
                        action {
                            val k = torad(10.0)
                            scene1.models[0].transform(Vector3(0.0, 0.0, 0.0), Vector3(1.0, 1.0, 1.0), Vector3(0.0, k, 0.0))
                            render.renderScene(scene1, visible)
                        }
                    }
                }
                button {
                    text = "down"
                    action {
                        val k = torad(10.0)
                        scene1.models[0].transform(Vector3(0.0, 0.0, 0.0), Vector3(1.0, 1.0, 1.0), Vector3(-k, 0.0, 0.0))
                        render.renderScene(scene1, visible)
                    }
                }
            }
        }
        vbox {
            alignment = Pos.CENTER_LEFT
            spacing = 10.0
            run {
                label { text = "Скрыть:" }
                checkbox("Грани").action {

                    visible[0] = !visible[0]
                    render.renderScene(scene1, visible)
                }
                checkbox("Ребра").action {
                    visible[1] = !visible[1]
                    render.renderScene(scene1, visible)
                }
                checkbox("Вершины").action {
                    visible[2] = !visible[2]
                    render.renderScene(scene1, visible)
                }
            }
        }
        vbox {
            alignment = Pos.CENTER_LEFT
            spacing = 10.0
            run {
                label { text = "При проведении мышкой:" }
                togglebutton ("Поворот") {
                    action {
                        text = if (isSelected) "Перемещение" else "Поворот"
                        if (isSelected) {
                            transform_flags[0] = true
                            transform_flags[1] = false
                        }
                        else {
                            transform_flags[1] = true
                            transform_flags[0] = false
                        }
                    }
                }
            }
        }
        button {
            text = "right"
            action {
                scene1.models[0].transform(Vector3(10.0, 0.0, 0.0), Vector3(1.0, 1.0, 1.0), Vector3(0.0, 0.0, 0.0))
                render.renderScene(scene1, visible)
            }
        }
        button {
            text = "down"
            action {
                scene1.models[0].transform(Vector3(0.0, 10.0, 0.0), Vector3(1.0, 1.0, 1.0), Vector3(0.0, 0.0, 0.0))
                render.renderScene(scene1, visible)
            }
        }
    }
}