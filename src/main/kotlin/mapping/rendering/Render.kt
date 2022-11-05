package mapping.rendering

import javafx.scene.paint.Color
import javafx.scene.image.WritableImage
import mapping.defines.BackgroundColor
import mapping.math.Vector3
import mapping.objects.camera.Camera
import mapping.objects.model.parts.Facet
import mapping.scene.Scene
import tornadofx.Vector2D

class Render (image: WritableImage) {
    val wimage = image
    val zBuffer = Array(wimage.width.toInt()) {DoubleArray(wimage.height.toInt()) {Double.MIN_VALUE} }
    val pw = wimage.pixelWriter
    val width = wimage.width
    val height = wimage.height.toInt()

    private fun initBuffers() {
        for (k in zBuffer) {
            k.fill(Double.MIN_VALUE)
            //k.fill(-1000000.0)
        }
        for (i in 0 until  width.toInt())
            for (j in 0 until height.toInt())
                pw.setColor(i, j, BackgroundColor.color)
    }

    private fun processPixel (p: Vector3, c: Color) {
        val x = p.x.toInt()
        val y = p.y.toInt()
       // println(y)
        if (x <= 0 || x >= width - 1)
            return
        if (y <= 1 || y >= height - 1)
            return
        if (p.z >= zBuffer[x][y]) {
            zBuffer[x][y] = p.z
            pw.setColor(x, height - y, c)
        }
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
        val fraction = n.z
        //println(fraction)
        val red = (col.red * fraction * 255).toInt()
//        if (red > 255)
//            red = 255
//        if (red < 0)
//            red = 0

        val green = (col.green * fraction * 255).toInt()
//        if (green > 255)
//            green = 255
//        if (green < 0)
//            green = 0

        val blue = (col.blue * fraction * 255).toInt()
//        if (blue > 255)
//            blue = 255
//        if (blue < 0)
//            blue = 0
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

    private fun processFacet(facets: MutableList<Vector3>, rect: IntArray, color: Color) {
        val square = (facets[0].y - facets[2].y) * (facets[1].x - facets[2].x) +
                     (facets[1].y - facets[2].y) * (facets[2].x - facets[0].x)
        for (y in rect[1] .. rect[3]) {
            for (x in rect[0]..rect[2]) {
                val barCoords = calcBarycentric(x, y, facets, square)
                if (barCoords.x >= -1e-5 && barCoords.y >= -1e-5 && barCoords.z >= -1e-5) {
                    val z = baryCentricInterpol(facets[0], facets[1], facets[2], barCoords)
                    processPixel(Vector3(x.toDouble(), y.toDouble(), z), color)
                }
            }
        }
    }

    private fun renderFacet (facet: Facet, scene: Scene, screenCenter: Vector2D) {
        val normal = facet.getNormal(scene.camera, screenCenter)
        if (normal.z < 0) return
        val screenFacets = mutableListOf<Vector3>()
        for (v in facet.dots) {
            screenFacets += v.getScreenPos(scene.camera, screenCenter)
        }
        val frameRect = calculateFrameRect(screenFacets)
        val faceColor = calculateFacetColor(facet, scene.camera, screenCenter)
        processFacet(screenFacets, frameRect, faceColor)
    }

    private fun processLine(p1: Vector3, p2: Vector3, color: Color) {
        //TODO: ну мб и можно сделать
    }

    fun renderScene (scene: Scene) {
        initBuffers()


        for (model in scene.models) {
            //Грани
            for (facet in model.poligons.facets)
                renderFacet(facet, scene, Vector2D(wimage.width / 2, wimage.height / 2))

            //TODO: ребра
            //TODO: вершины
        }
    }
}