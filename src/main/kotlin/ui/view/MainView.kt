package ui.view

import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import javafx.scene.control.ToggleGroup
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import mapping.fileload.FileManager
import mapping.math.Vector3
import mapping.rendering.Render
import tornadofx.*
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt

class MainView : View("MainWindow") {
    private val image = Image("/drawimage.jpg")
    private val wimage = WritableImage(image.pixelReader, image.width.toInt(), image.height.toInt())
    val render = Render(wimage)
    val fileMan = FileManager()
    var scene1 = fileMan.loadScene("/home/zorox/Документы/bmstu_iu7_cg_cw/src/main/resources/io.sol", 200.0)
    val visible = Array(3) {true}
    val transform_flags = Array(2) {false}

    var trans_of_model = false
    val trans_of_model_flag = Array(3) {false}
    private val transToggleCroup = ToggleGroup()

    var pressedX = 0.0
    var pressedY = 0.0

    private fun torad(rad: Double) :Double {
        return rad / 180 * 3.14
    }

    override val root = hbox {
        imageview(wimage).apply {

            this.setOnMousePressed {
                if (!trans_of_model) {
                    pressedX = it.x
                    pressedY = it.y
                }
                else {
                    if (trans_of_model_flag[0]) {
                        var len = 1400.0
                        var p = 0
                        for (ver in 0 until  scene1.models[0].poligons.vertices.size) {
                            val screenPos = scene1.models[0].poligons.vertices[ver].getScreenPos(scene1.camera, Vector2D(wimage.width / 2, wimage.height / 2))
                            if (render.checkPixel(screenPos)) {
                                val ddx = it.x - screenPos.x
                                val ddy = wimage.height - it.y - screenPos.y
                                if (sqrt(ddx * ddx + ddy * ddy) < len) {
                                    len = sqrt(ddx * ddx + ddy * ddy)
                                    p = ver
                                }
                            }
                        }
                        for (ver in scene1.models[0].poligons.vertices)
                            ver.selected = false
                        scene1.models[0].poligons.vertices[p].selected = true
                        render.renderScene(scene1, visible)
                    }
                }
            }

            this.setOnMouseDragged {
                val dx = it.x - pressedX
                val dy = it.y - pressedY
                pressedY = it.y
                pressedX = it.x

                if (!trans_of_model) {
                    if (transform_flags[0]) {
                        val move_params = Vector3(dx, -dy, 0.0)
                        scene1.models[0].transform(move_params, Vector3(1.0, 1.0, 1.0), Vector3(0.0, 0.0, 0.0))
                        render.renderScene(scene1, visible)
                    } else if (transform_flags[1]) {
                        val rotate_params = Vector3(-torad(dy) * 0.1, -torad(dx) * 0.1, 0.0)
                        scene1.models[0].transform(Vector3(0.0, 0.0, 0.0), Vector3(1.0, 1.0, 1.0), rotate_params)
                        render.renderScene(scene1, visible)
                    }
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

        vbox {
            alignment = Pos.CENTER_LEFT
            spacing = 10.0
            run {
                label { text = "Преобразования модели" }
                checkbox("Начать преобразование").action {
                    trans_of_model = !trans_of_model
                }

                togglebutton("Вершина", transToggleCroup).action {
                    trans_of_model_flag[0] = true
                    trans_of_model_flag[1] = false
                    trans_of_model_flag[2] = false
                }
                togglebutton("Ребро", transToggleCroup).action {
                    trans_of_model_flag[0] = false
                    trans_of_model_flag[1] = true
                    trans_of_model_flag[2] = false
                }
                togglebutton("Грань", transToggleCroup).action {
                    trans_of_model_flag[0] = false
                    trans_of_model_flag[1] = false
                    trans_of_model_flag[2] = true
                }

            }
        }
    }
}