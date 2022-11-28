package ui.view

import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import javafx.scene.control.ToggleGroup
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import javafx.scene.text.Font
import mapping.fileload.Chooser
import mapping.fileload.FileManager
import mapping.math.Vector3
import mapping.objects.model.parts.Edge
import mapping.objects.model.parts.Facet
import mapping.objects.model.parts.Vertex
import mapping.rendering.Render
import tornadofx.*
import java.awt.FileDialog
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class MainView : View("MainWindow") {
    private val image = Image("/drawimage.jpg")
    private val wimage = WritableImage(image.pixelReader, image.width.toInt(), image.height.toInt())
    val render = Render(wimage)
    val fileMan = FileManager()
    var scene1 = fileMan.loadScene("/home/zorox/Документы/bmstu_iu7_cg_cw/src/main/resources/empty.sol", 200.0)


    val visible = Array(3) {true}
    val transform_flags = Array(2) {false}

    var trans_of_model = false
    val trans_of_model_flag = Array(3) {false}
    private val transToggleCroup = ToggleGroup()
    private val transModelToggleGroup = ToggleGroup()

    private var handle_transform_flag = false

    private var creation_flag = false
    private var deleting_flag = false

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
                    val fln = Chooser.saveFile()
                    if (fln == "0")
                        WarningView("Ошибка при выборе файла!").openWindow()
                    else
                        fileMan.saveModel(fln, scene1.camera, scene1.models[0])
                }
                item("Open", "Shortcut+O").action {
                    val fln = Chooser.openFile()
                    if (fln == "0") {
                        WarningView("Ошибка при выборе файла!").openWindow()
                    }
                    else {
                        scene1 = fileMan.loadScene(fln, 200.0)
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
                        render.renderScene(scene1, visible)
                    }
                }
                item("New file", "Shortcut+N").action {
                    NewFileView(scene1, fileMan).openWindow()
                }
                item("Exit").action {
                    close()
                }
            }
        }
        hbox {
            alignment = Pos.TOP_LEFT
            spacing = 20.0
            imageview(wimage).apply {

                this.setOnMousePressed{

                    if (it.isSecondaryButtonDown) {
                        val p = find_nearest_dot(it.x, it.y)
                        val scr = scene1.models[0].poligons.vertices[p].getScreenPos(scene1.camera, Vector2D(.0, .0))
                        val px = scr.x
                        val py = scr.y
                        val pz = scr.z
                        InfoView("X = $px\nY = $py\nZ = $pz").openWindow()
                    }
                    if (it.isPrimaryButtonDown) {
                        if (trans_of_model) {
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
                        } else if (creation_flag) {
                            val p = find_nearest_dot(it.x, it.y)
                            var col = 0
                            for (v in scene1.models[0].poligons.vertices)
                                if (v.in_creating)
                                    col++
                            if (col < 3)
                                scene1.models[0].poligons.vertices[p].in_creating = true
                            render.renderScene(scene1, visible)
                        }
                    }
                    pressedX = it.x
                    pressedY = it.y
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
                            val rotate_params = Vector3(-dy * 0.001, -dx * 0.001, 0.0)
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
                        label { text = "Поворот" }
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
                                text = "влево "
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
                            text = " вниз "
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

                vbox {
                    label { text = "Раскрасить грани" }
                    colorpicker {
                        this.setOnAction {
                            for (f in scene1.models[0].poligons.facets)
                                if (f.selected)
                                    f.color = this.value
                            render.renderScene(scene1, visible)
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

                                text = "влево "
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
                            text = " вниз "
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
                                    WarningView("Неверный ввод!").openWindow()
                                else {
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
                                            WarningView("Неверный ввод!").openWindow()
                                        else {
                                            for (v in scene1.models[0].poligons.vertices)
                                                if (v.selected)
                                                    v.move(Vector3(move_x!!, move_y!!, move_z!!))
                                            render.renderScene(scene1, visible)
                                        }
                                    }
                                }
                            }
//                            vbox {
//                                //TODO: спросить, что вообще с вращением граней и тд делать
//                                label { text = "Поворот" }
//                                textfield("0.0") {
//                                    tooltip("Преобразование по X в градусах") { font = Font.font("Verdana") }
//                                    textProperty().addListener { obj, old, new ->
//                                        rotate_x = this.text.toDoubleOrNull()
//                                    }
//                                }
//                                textfield("0.0") {
//                                    tooltip("Преобразование по Y в градусах") { font = Font.font("Verdana") }
//                                    textProperty().addListener { obj, old, new ->
//                                        rotate_y = this.text.toDoubleOrNull()
//                                    }
//                                }
//                                textfield("0.0") {
//                                    tooltip("Преобразование по Z в градусах") { font = Font.font("Verdana") }
//                                    textProperty().addListener { obj, old, new ->
//                                        rotate_z = this.text.toDoubleOrNull()
//                                    }
//                                }
//                            }
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

            //колонка созидания
            vbox {
                alignment = Pos.TOP_LEFT
                spacing = 20.0
                label { text = "Создание новых элементов" }
                checkbox("Начать создание полигона").action {
                    creation_flag = !creation_flag
                    if (!creation_flag)
                        for (v in scene1.models[0].poligons.vertices)
                            v.in_creating = false
                    render.renderScene(scene1, visible)
                }

                var newx : Double? = 0.0
                var newy : Double? = 0.0
                var newz : Double? = 0.0

                vbox {
                    label { text = "Координаты новой точки" }
                    textfield("0.0") {
                        tooltip("Координата X") { font = Font.font("Verdana") }
                        textProperty().addListener { obj, old, new ->
                            newx = this.text.toDoubleOrNull()
                        }
                    }
                    textfield("0.0") {
                        tooltip("Координата Y") { font = Font.font("Verdana") }
                        textProperty().addListener { obj, old, new ->
                            newy = this.text.toDoubleOrNull()
                        }
                    }
                    textfield("0.0") {
                        tooltip("Координата Z") { font = Font.font("Verdana") }
                        textProperty().addListener { obj, old, new ->
                            newz = this.text.toDoubleOrNull()
                        }
                    }
                }

                button {
                    this.text = "Создать вершину"
                    action {
                        if (newx == null || newy == null || newz == null)
                            WarningView("Неверный ввод!").openWindow()
                        else {
                            scene1.models[0].poligons.vertices += Vertex(Vector3(newx!!, newy!!, newz!!))
                            scene1.models[0].poligons.vertices[scene1.models[0].poligons.vertices.size - 1].in_creating = true
                            render.renderScene(scene1, visible)
                        }
                    }
                }

                button {
                    this.text = "Создать полигон"
                    action {
                        var col = 0
                        for (v in scene1.models[0].poligons.vertices)
                            if (v.in_creating)
                                col++
                        if (col != 3)
                            WarningView("В полигоне должно быть 3 вершины!").openWindow()
                        else {
                            val dots = mutableListOf<Vertex>()
                            for (v in scene1.models[0].poligons.vertices)
                                if (v.in_creating) {
                                    dots += v
                                    v.in_creating = false
                                }
                            val edges = mutableListOf<Edge>()
                            edges += Edge(dots[0], dots[1])
                            edges += Edge(dots[2], dots[1])
                            edges += Edge(dots[0], dots[2])
                            val ar = Array(3) {true}
                            for (v in scene1.models[0].poligons.vertices) {
                                for (i in 0..2) {
                                    if (dots[i] === v)
                                        ar[i] = false
                                }
                            }
                            val ar2 = Array(3) {true}
                            for (e in scene1.models[0].poligons.edges) {
                                for (i in 0..2) {
                                    if ((e.id_p2 === edges[i].id_p1 && e.id_p1 === edges[i].id_p2) || (e.id_p1 === edges[i].id_p1 && e.id_p2 === edges[i].id_p2))
                                        ar2[i] = false
                                }
                            }
                            for (i in 0..2) {
                                if (ar[i])
                                    scene1.models[0].poligons.vertices += dots[i]
                                if (ar2[i])
                                    scene1.models[0].poligons.edges += edges[i]
                            }
                            scene1.models[0].poligons.facets += Facet(dots, edges, Color.WHITE)
                            render.renderScene(scene1, visible)
                        }
                    }
                }

                checkbox("Удаление полигона").action {
                    deleting_flag = !deleting_flag
                }
                button ("Удалить полигон").action {
                    println("start")
                    var p = -1
                    for (f in 0 until scene1.models[0].poligons.facets.size)
                        if (scene1.models[0].poligons.facets[f].selected) {
                            p = f
                            break
                        }
                    if (p != -1) {
                        val dots = scene1.models[0].poligons.facets[p].dots
                        val edges = scene1.models[0].poligons.facets[p].edges
                        scene1.models[0].poligons.facets.removeAt(p)
                        val dp = Array(3){-1}
                        val ep = Array(3){-1}
                        for (i in 0..2) {
                            for (e in 0 until scene1.models[0].poligons.edges.size)
                                if (edges[i] === scene1.models[0].poligons.edges[e])
                                    ep[i] = e
                            for (v in 0 until scene1.models[0].poligons.vertices.size)
                                if (dots[i] === scene1.models[0].poligons.vertices[v])
                                    dp[i] = v
                        }
                        for (k in 0..2) {
                            for (f in scene1.models[0].poligons.facets) {
                                for (e in f.edges)
                                    if (e === edges[k])
                                        ep[k] = -1
                                for (d in f.dots)
                                    if (d === dots[k])
                                        dp[k] = -1
                                if (ep[k] == -1 && dp[k] == -1)
                                    break
                            }
                        }
                        var mmep = max(ep[0], max(ep[1], ep[2]))
                        while (mmep != -1) {
                            scene1.models[0].poligons.edges.removeAt(mmep)
                            for (e in ep.indices)
                                if (ep[e] == mmep)
                                    ep[e] = -1
                            mmep = max(ep[0], max(ep[1], ep[2]))
                        }
                        var mmvp = max(dp[0], max(dp[1], dp[2]))
                        while (mmvp != -1) {
                            scene1.models[0].poligons.vertices.removeAt(mmvp)
                            for (d in dp.indices)
                                if (dp[d] == mmvp)
                                    dp[d] = -1
                            mmvp = max(dp[0], max(dp[1], dp[2]))
                        }
                        full_not_selected()
                        render.renderScene(scene1, visible)
                    }
                    else
                        WarningView("Выберите полигон!").openWindow()
                }
            }
        }
    }
}