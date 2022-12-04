package mapping.rendering

import javafx.scene.paint.Color
import javafx.scene.image.WritableImage
import mapping.defines.BackgroundColor
import mapping.math.Vector3
import mapping.objects.camera.Camera
import mapping.objects.model.parts.Facet
import mapping.scene.Scene
import tornadofx.Vector2D
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

class Render (image: WritableImage) {
    val wimage = image
    val zBuffer = Array(wimage.width.toInt()) {DoubleArray(wimage.height.toInt()) {- Double.MAX_VALUE} }
    val pw = wimage.pixelWriter
    val width = wimage.width
    val height = wimage.height.toInt()

    private fun initBuffers() {
        for (k in zBuffer) {
            k.fill(- Double.MAX_VALUE)
            //k.fill(-1000000.0)
        }
        for (i in 0 until  width.toInt())
            for (j in 0 until height.toInt())
                pw.setColor(i, j, BackgroundColor.color)
    }

    fun processPixel (p: Vector3, c: Color, visible: Boolean) {
        val x = p.x.roundToInt()
        val y = p.y.roundToInt()
       // println(y)
        if (x <= 0 || x >= width - 1)
            return
        if (y <= 1 || y >= height - 1)
            return
        if (p.z >= zBuffer[x][y]) {
            zBuffer[x][y] = p.z
            if (visible)
                pw.setColor(x, height - y, c)
        }
    }

    fun checkPixel(p: Vector3) : Boolean {
        val x = p.x.toInt()
        val y = p.y.toInt()
        // println(y)
        if (x <= 3 || x >= width - 3)
            return false
        if (y <= 3 || y >= height - 3)
            return false
        if (p.z + 2 >= zBuffer[x][y]) {
            return true
        }
        return false
    }

    private fun calculateFrameRect (facets: MutableList<Vector3>) : IntArray {
        val p = facets[0]
        var maxX = p.x
        var maxY = p.y
        var minX = maxX
        var minY = maxY
        for (v in facets) {
            if (v.x > maxX) maxX = v.x
            if (v.y > maxY) maxY = v.y
            if (v.x < minX) minX = v.x
            if (v.y < minY) minY = v.y
        }
        val arr = IntArray(4)
        arr[0] = minX.toInt()
        arr[1] = minY.toInt()
        arr[2] = maxX.toInt()
        arr[3] = maxY.toInt()
        return arr
    }
    
    private fun calculateFacetColor(facet: Facet, camera: Camera, screenCenter: Vector2D) : Color {
        val n = facet.getNormal(camera, screenCenter)
        val col = facet.color
        val fraction = abs(n.z)
        val red = (col.red * fraction * 255).toInt()
        val green = (col.green * fraction * 255).toInt()
        val blue = (col.blue * fraction * 255).toInt()
        //TODO: уточнить, не будет ли слишком сильно затемняться
        return Color.rgb(red, green, blue)
    }

    private fun calcBarycentric(x: Int, y: Int, triangle: MutableList<Vector3>, square: Double) : Vector3 {
        val vx = ((y - triangle[2].y) * (triangle[1].x - triangle[2].x) + (triangle[1].y - triangle[2].y) * (triangle[2].x - x)) / square
        val vy = ((y - triangle[0].y) * (triangle[2].x - triangle[0].x) + (triangle[2].y - triangle[0].y) * (triangle[0].x - x)) / square
        val z = 1 - vx - vy
        return Vector3(vx, vy, z)
    }

    private fun baryCentricInterpol(a: Vector3, b: Vector3, c: Vector3, bary: Vector3): Double {
        return bary.x * a.z + bary.y * b.z + bary.z * c.z
    }

    private fun processFacet(facets: MutableList<Vector3>, rect: IntArray, color: Color, visible: Boolean) {
        val square = (facets[0].y - facets[2].y) * (facets[1].x - facets[2].x) +
                     (facets[1].y - facets[2].y) * (facets[2].x - facets[0].x)
        for (y in rect[1] .. rect[3]) {
            for (x in rect[0]..rect[2]) {
                val barCoords = calcBarycentric(x, y, facets, square)
                if (barCoords.x >= -1e-5 && barCoords.y >= -1e-5 && barCoords.z >= -1e-5) {
                    val z = baryCentricInterpol(facets[0], facets[1], facets[2], barCoords)
                    processPixel(Vector3(x.toDouble(), y.toDouble(), z), color, visible)
                }
            }
        }
    }

    private fun renderFacet (facet: Facet, scene: Scene, screenCenter: Vector2D, visible: Boolean) {
        val screenFacets = mutableListOf<Vector3>()
        for (v in facet.dots) {
            screenFacets += v.getScreenPos(scene.camera, screenCenter)
        }
        val frameRect = calculateFrameRect(screenFacets)
        var faceColor = calculateFacetColor(facet, scene.camera, screenCenter)
        if (facet.selected) {
            if (facet.color == Color.BLUE)
                faceColor = Color.ORANGE
            else
                faceColor = Color.BLUE
        }
        processFacet(screenFacets, frameRect, faceColor, visible)
    }

    private fun processLine(p1: Vector3, p2: Vector3, color: Color = Color.BLACK, visible: Boolean) {
        val xStart = p1.x
        val xEnd = p2.x
        val yStart = p1.y
        val yEnd = p2.y
        val zStart = p1.z
        val zEnd = p2.z

        if (xStart == xEnd && yStart == yEnd) {
            if (zStart > zEnd)
                processPixel(p1, color, visible)
            else
                processPixel(p2, color, visible)
            return
        }

        var delX = xEnd - xStart
        var delY = yEnd - yStart
        var delZ = zEnd - zStart

        val length = max(abs(delX), abs(delY))

        delX /= length
        delY /= length
        delZ /= length

        var curX = xStart
        var curY = yStart
        var curZ = zStart

        for (i in 0 until length.toInt()) {
            processPixel(Vector3(curX, curY, curZ + 1), color, visible)
            curX += delX
            curY += delY
            curZ += delZ
        }
    }

    fun renderScene (scene: Scene, visible: Array<Boolean>) {
        initBuffers()

        for (model in scene.models) {
            //Грани

            val begin = System.nanoTime()
            for (facet in model.poligons.facets)
                renderFacet(facet, scene, Vector2D(wimage.width / 2, wimage.height / 2), visible[0])
            val end = System.nanoTime()
            val st = end - begin
            println("render: $st")

            if (visible[1])
                for (edge in model.poligons.edges) {
                    var c = Color.BLACK
                    if (edge.selected)
                        c = Color.BLUE
                    processLine(
                        edge.id_p1.getScreenPos(scene.camera, Vector2D(wimage.width / 2, wimage.height / 2)),
                        edge.id_p2.getScreenPos(scene.camera, Vector2D(wimage.width / 2, wimage.height / 2)),
                        c, visible[1])
                }
            if (visible[2]) {
                for (ver in model.poligons.vertices) {
                    val screenPos = ver.getScreenPos(scene.camera, Vector2D(wimage.width / 2, wimage.height / 2))
                    if (checkPixel(screenPos)) {
                        val x = screenPos.x.toInt()
                        val y = screenPos.y.toInt()
                        var c = Color.BLACK
                        if (ver.selected)
                            c = Color.BLUE
                        if (ver.in_creating)
                            c = Color.ORANGE

                        pw.setColor(x, height - y, c)

                        pw.setColor(x + 1, height - y, c)
                        pw.setColor(x - 1, height - y, c)
                        pw.setColor(x, height - y + 1, c)
                        pw.setColor(x, height - y - 1, c)

                        pw.setColor(x + 2, height - y, c)
                        pw.setColor(x - 2, height - y, c)
                        pw.setColor(x, height - y + 2, c)
                        pw.setColor(x, height - y - 2, c)

                        pw.setColor(x + 1, height - y + 1, c)
                        pw.setColor(x - 1, height - y - 1, c)
                        pw.setColor(x - 1, height - y + 1, c)
                        pw.setColor(x + 1, height - y - 1, c)
                    }
                }
            }
        }
    }
}