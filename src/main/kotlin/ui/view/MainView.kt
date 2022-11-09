package ui.view

import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import javafx.scene.control.ToggleGroup
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.text.Font
import mapping.fileload.Chooser
import mapping.fileload.FileManager
import mapping.math.Vector3
import mapping.rendering.Render
import tornadofx.*
import java.awt.FileDialog
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt

class MainView : View("MainWindow") {
    private val image = Image("/drawimage.jpg")
    private val wimage = WritableImage(image.pixelReader, image.width.toInt(), image.height.toInt())
    val render = Render(wimage)
    val fileMan = FileManager()
    var scene1 = fileMan.loadScene("/home/zorox/Документы/bmstu_iu7_cg_cw/src/main/resources/say1.sol", 200.0)


    val visible = Array(3) {true}
    val transform_flags = Array(2) {false}

    var trans_of_model = false
    val trans_of_model_flag = Array(3) {false}
    private val transToggleCroup = ToggleGroup()
    private val transModelToggleGroup = ToggleGroup()

    private var handle_transform_flag = false

    private var p_edge_1 = -1
    private var p_facet_2 = -1
    private var e_facet_1 = -1
    private var e_facet_2 = -1

    var pressedX = 0.0
    var pressedY = 0.0


    private fun torad(rad: Double) :Double {
        return rad / 180 * 3.14
    }

    private fun find_nearest_dot (x: Double, y: Double) : Int {
        var len = 1400.0
        var p = 0
        for (ver in 0 until scene1.models[0].poligons.vertices.size) {
            val screenPos = scene1.models[0].poligons.vertices[ver].getScreenPos(
                scene1.camera,
                Vector2D(wimage.width / 2, wimage.height / 2)
            )
            if (render.checkPixel(screenPos)) {
                val ddx = x - screenPos.x
                val ddy = wimage.height - y - screenPos.y
                if (sqrt(ddx * ddx + ddy * ddy) < len) {
                    len = sqrt(ddx * ddx + ddy * ddy)
                    p = ver
                }
            }
        }
        return p
    }

    private fun find_edge_for_v(p1: Int, p2: Int) : Int {
        if (p1 == -1 || p2 == -1)
            return -1
        val v1 = scene1.models[0].poligons.vertices[p1]
        val v2 = scene1.models[0].poligons.vertices[p2]
        var flag = false
        for (ek in 0 until scene1.models[0].poligons.edges.size) {
            val e = scene1.models[0].poligons.edges[ek]
            flag = (v1 === e.id_p1 && v2 === e.id_p2) || (v1 === e.id_p2 && v2 === e.id_p1)
            if (flag) {
                full_not_selected()
                scene1.models[0].poligons.vertices[p1].selected = true
                scene1.models[0].poligons.vertices[p2].selected = true
                e.selected = true
                return ek
            }
        }
        return -1
    }

    private fun find_facet_for_e(e1: Int, e2:Int): Int {
        if (e1 == -1 || e2 == -1)
            return -1
        val ed1 = scene1.models[0].poligons.edges[e1]
        val ed2 = scene1.models[0].poligons.edges[e2]
        var flag = false
        for (fk in 0 until scene1.models[0].poligons.facets.size) {
            val f = scene1.models[0].poligons.facets[fk]
            flag = (ed1 == f.edges[0]) || (ed1 == f.edges[1]) || (ed1 == f.edges[2])
            flag = flag && ((ed2 == f.edges[0]) || (ed2 == f.edges[1]) || (ed2 == f.edges[2]))
            if (flag) {
                full_not_selected()
                for (v in f.dots)
                    v.selected = true
                for (e in f.edges)
                    e.selected = true
                f.selected = true
                return fk
            }
        }
        return -1
    }

    private fun full_not_selected() {
        for (fa in scene1.models[0].poligons.facets) {
            fa.selected = false
        }
        for (ea in scene1.models[0].poligons.edges) {
            ea.selected = false
        }
        for (va in scene1.models[0].poligons.vertices) {
            va.selected = false
        }
    }

    override val root = vbox {
        alignment = Pos.TOP_LEFT
        spacing = 0.0
        menubar {
            menu("File") {
                item("Save", "Shortcut+S").action {
                    println("Not done yet!")
                    fileMan.saveModel(Chooser.saveFile(), scene1.camera, scene1.models[0])
                }
                item("Open", "Shortcut+O").action {
                    println("Not done yet!")
                    Chooser.openFile()
                }
                item("Exit")
            }
        }
        hbox {
            alignment = Pos.TOP_LEFT
            spacing = 20.0
            imageview(wimage).apply {

                this.setOnMousePressed {
                    if (!trans_of_model) {
                        pressedX = it.x
                        pressedY = it.y
                    } else {
                        full_not_selected()
                        if (trans_of_model_flag[0]) {
                            val p = find_nearest_dot(it.x, it.y)
                            scene1.models[0].poligons.vertices[p].selected = true
                            render.renderScene(scene1, visible)
                        } else if (trans_of_model_flag[1]) {
                            val p = find_nearest_dot(it.x, it.y)
                            if (p_edge_1 == -1) {
                                p_edge_1 = p
                                scene1.models[0].poligons.vertices[p].selected = true
                            } else {
                                scene1.models[0].poligons.vertices[p].selected = true
                                e_facet_1 = find_edge_for_v(p, p_edge_1)
                                if (e_facet_1 == -1) {
                                    p_edge_1 = -1
                                }
                                e_facet_1 = -1
                            }
                            render.renderScene(scene1, visible)
                        } else if (trans_of_model_flag[2]) {
                            val p = find_nearest_dot(it.x, it.y)
                            if (p_edge_1 == -1) {
                                p_edge_1 = p
                                scene1.models[0].poligons.vertices[p].selected = true
                            } else if (p_facet_2 == -1) {
                                p_facet_2 = p
                                scene1.models[0].poligons.vertices[p].selected = true
                                e_facet_1 = find_edge_for_v(p, p_edge_1)
                                if (e_facet_1 == -1) {
                                    p_edge_1 = -1
                                }
                            } else {
                                scene1.models[0].poligons.vertices[p].selected = true
                                e_facet_2 = find_edge_for_v(p, p_facet_2)
                                if (e_facet_2 == -1) {
                                    p_edge_1 = -1
                                    e_facet_1 = -1
                                    p_facet_2 = -1
                                } else {
                                    val fac = find_facet_for_e(e_facet_1, e_facet_2)
                                    if (fac == -1) {
                                        p_edge_1 = -1
                                        e_facet_1 = -1
                                        p_facet_2 = -1
                                    }
                                }
                            }
                        }
                        render.renderScene(scene1, visible)
                    }
                }

                this.setOnMouseDragged {
                    val dx = it.x - pressedX
                    val dy = it.y - pressedY
                    pressedY = it.y
                    pressedX = it.x
                    if (abs(dx) > 100.0 || abs(dy) > 100.0)
                        return@setOnMouseDragged
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
                    if (handle_transform_flag) {
                        for (v in scene1.models[0].poligons.vertices)
                            if (v.selected)
                                v.move(Vector3(dx, -dy, .0))
                        render.renderScene(scene1, visible)
                    }
                }

                var cenw = wimage.width / 2
                val cenh = wimage.height / 2
                if (cenw > cenh)
                    cenw = cenh
                cenw /= 2

                scene1.models[0].transform(
                    Vector3(cenw * 2, cenw * 2, .0),
                    Vector3(cenw, cenw, cenw),
                    Vector3(0.0, 0.0, 0.0)
                )
                scene1.models[0].poligons.setArithCenter()
                render.renderScene(scene1, visible)
            }
            hboxConstraints {
                prefWidth = 1000.0
                prefHeight = 1000.0
            }
            vbox {
                alignment = Pos.TOP_CENTER
                spacing = 20.0
                vbox {
                    alignment = Pos.CENTER
                    spacing = 10.0
                    run {
                        label { text = "Верчение" }
                        button {
                            text = "вверх"
                            action {
                                val k = torad(10.0)
                                scene1.models[0].transform(
                                    Vector3(0.0, 0.0, 0.0),
                                    Vector3(1.0, 1.0, 1.0),
                                    Vector3(k, 0.0, 0.0)
                                )
                                render.renderScene(scene1, visible)
                            }
                        }
                        hbox {
                            alignment = Pos.CENTER
                            spacing = 10.0
                            button {
                                text = "влево"
                                action {
                                    val k = torad(10.0)
                                    scene1.models[0].transform(
                                        Vector3(0.0, 0.0, 0.0),
                                        Vector3(1.0, 1.0, 1.0),
                                        Vector3(0.0, k, 0.0)
                                    )
                                    render.renderScene(scene1, visible)
                                }
                            }
                            button {
                                text = "вправо"
                                action {
                                    val k = torad(10.0)
                                    scene1.models[0].transform(
                                        Vector3(0.0, 0.0, 0.0),
                                        Vector3(1.0, 1.0, 1.0),
                                        Vector3(0.0, -k, 0.0)
                                    )
                                    render.renderScene(scene1, visible)
                                }
                            }
                        }
                        button {
                            text = "вниз"
                            action {
                                val k = torad(10.0)
                                scene1.models[0].transform(
                                    Vector3(0.0, 0.0, 0.0),
                                    Vector3(1.0, 1.0, 1.0),
                                    Vector3(-k, 0.0, 0.0)
                                )
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
                        togglebutton("Поворот", transModelToggleGroup).action {
                            transform_flags[0] = false
                            transform_flags[1] = true
                        }
                        togglebutton("Перемещение", transModelToggleGroup).action {
                            transform_flags[0] = true
                            transform_flags[1] = false
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

            //новая колонка
            vbox {
                alignment = Pos.TOP_CENTER
                spacing = 20.0
                vbox {
                    alignment = Pos.CENTER
                    spacing = 10.0
                    run {
                        label { text = "Перемещение" }
                        button {
                            text = "вверх"
                            action {
                                scene1.models[0].transform(
                                    Vector3(0.0, 10.0, 0.0),
                                    Vector3(1.0, 1.0, 1.0),
                                    Vector3(0.0, 0.0, 0.0)
                                )
                                render.renderScene(scene1, visible)
                            }
                        }
                        hbox {
                            alignment = Pos.CENTER
                            spacing = 10.0
                            button {
                                text = "влево"
                                action {
                                    scene1.models[0].transform(
                                        Vector3(-10.0, 0.0, 0.0),
                                        Vector3(1.0, 1.0, 1.0),
                                        Vector3(0.0, 0.0, 0.0)
                                    )
                                    render.renderScene(scene1, visible)
                                }
                            }
                            button {
                                text = "вправо"
                                action {
                                    scene1.models[0].transform(
                                        Vector3(10.0, 0.0, 0.0),
                                        Vector3(1.0, 1.0, 1.0),
                                        Vector3(0.0, 0.0, 0.0)
                                    )
                                    render.renderScene(scene1, visible)
                                }
                            }
                        }
                        button {
                            text = "вниз"
                            action {
                                scene1.models[0].transform(
                                    Vector3(0.0, -10.0, 0.0),
                                    Vector3(1.0, 1.0, 1.0),
                                    Vector3(0.0, 0.0, 0.0)
                                )
                                render.renderScene(scene1, visible)
                            }
                        }
                    }
                }
                vbox {
                    alignment = Pos.CENTER
                    spacing = 10.0
                    run {
                        var move_x: Double? = 0.0
                        var move_y: Double? = 0.0
                        var move_z: Double? = 0.0
                        var rotate_x: Double? = 0.0
                        var rotate_y: Double? = 0.0
                        var rotate_z: Double? = 0.0
                        label { text = "Перемещение модели" }
                        hbox {
                            vbox {
                                label { text = "Движение" }
                                textfield("0.0") {
                                    tooltip("Преобразование по X") { font = Font.font("Verdana") }
                                    textProperty().addListener { obj, old, new -> move_x = this.text.toDoubleOrNull() }
                                }
                                textfield("0.0") {
                                    tooltip("Преобразование по Y") { font = Font.font("Verdana") }
                                    textProperty().addListener { obj, old, new -> move_y = this.text.toDoubleOrNull() }
                                }
                                textfield("0.0") {
                                    tooltip("Преобразование по Z") { font = Font.font("Verdana") }
                                    textProperty().addListener { obj, old, new -> move_z = this.text.toDoubleOrNull() }
                                }
                            }
                            vbox {
                                label { text = "Поворот" }
                                textfield("0.0") {
                                    tooltip("Преобразование по X в градусах") { font = Font.font("Verdana") }
                                    textProperty().addListener { obj, old, new ->
                                        rotate_x = this.text.toDoubleOrNull()
                                    }
                                }
                                textfield("0.0") {
                                    tooltip("Преобразование по Y в градусах") { font = Font.font("Verdana") }
                                    textProperty().addListener { obj, old, new ->
                                        rotate_y = this.text.toDoubleOrNull()
                                    }
                                }
                                textfield("0.0") {
                                    tooltip("Преобразование по Z в градусах") { font = Font.font("Verdana") }
                                    textProperty().addListener { obj, old, new ->
                                        rotate_z = this.text.toDoubleOrNull()
                                    }
                                }
                            }
                        }
                        button {
                            this.text = "Переместить"
                            action {
                                if (move_x == null || move_y == null || move_z == null)
                                    this.text = "Неверный ввод!"
                                else {
                                    this.text = "Переместить"
                                    //text("Преобразовать" )
                                    scene1.models[0].transform(
                                        Vector3(move_x!!, move_y!!, move_z!!),
                                        Vector3(1.0, 1.0, 1.0),
                                        Vector3(torad(rotate_x!!), torad(rotate_y!!), torad(rotate_z!!))
                                    )
                                    render.renderScene(scene1, visible)
                                }
                            }

                        }
                    }
                }

                vbox {
                    alignment = Pos.CENTER
                    spacing = 10.0
                    run {
                        var move_x: Double? = 0.0
                        var move_y: Double? = 0.0
                        var move_z: Double? = 0.0
                        var rotate_x: Double? = 0.0
                        var rotate_y: Double? = 0.0
                        var rotate_z: Double? = 0.0
                        label { text = "Преобразование модели" }
                        hbox {
                            vbox {
                                label { text = "Движение" }
                                textfield("0.0") {
                                    tooltip("Преобразование по X") { font = Font.font("Verdana") }
                                    textProperty().addListener { obj, old, new -> move_x = this.text.toDoubleOrNull() }
                                }
                                textfield("0.0") {
                                    tooltip("Преобразование по Y") { font = Font.font("Verdana") }
                                    textProperty().addListener { obj, old, new -> move_y = this.text.toDoubleOrNull() }
                                }
                                textfield("0.0") {
                                    tooltip("Преобразование по Z") { font = Font.font("Verdana") }
                                    textProperty().addListener { obj, old, new -> move_z = this.text.toDoubleOrNull() }
                                }
                                button {
                                    this.text = "Преобразовать"
                                    action {
                                        if (move_x == null || move_y == null || move_z == null)
                                            this.text = "Неверный ввод!"
                                        else {
                                            this.text = "Преобразовать"
                                            for (v in scene1.models[0].poligons.vertices)
                                                if (v.selected)
                                                    v.move(Vector3(move_x!!, move_y!!, move_z!!))
                                            render.renderScene(scene1, visible)
                                        }
                                    }
                                }
                            }
                            vbox {//TODO: спросить, что вообще с вращением граней и тд делать
                                label { text = "Поворот" }
                                textfield("0.0") {
                                    tooltip("Преобразование по X в градусах") { font = Font.font("Verdana") }
                                    textProperty().addListener { obj, old, new ->
                                        rotate_x = this.text.toDoubleOrNull()
                                    }
                                }
                                textfield("0.0") {
                                    tooltip("Преобразование по Y в градусах") { font = Font.font("Verdana") }
                                    textProperty().addListener { obj, old, new ->
                                        rotate_y = this.text.toDoubleOrNull()
                                    }
                                }
                                textfield("0.0") {
                                    tooltip("Преобразование по Z в градусах") { font = Font.font("Verdana") }
                                    textProperty().addListener { obj, old, new ->
                                        rotate_z = this.text.toDoubleOrNull()
                                    }
                                }
                            }
                        }
                        vbox {
                            alignment = Pos.CENTER
                            spacing = 10.0
                            run {
                                label { text = "Ручное преобразование модели" }
                                checkbox("Ручное преобразование").action {
                                    handle_transform_flag = !handle_transform_flag
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}