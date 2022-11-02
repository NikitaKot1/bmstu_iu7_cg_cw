package mapping.rendering

import javafx.scene.paint.Color
import javafx.scene.image.WritableImage
import mapping.math.Vector3
import mapping.objects.camera.Camera
import mapping.objects.model.parts.Facet
import mapping.scene.Scene
import tornadofx.Vector2D

class Render (image: WritableImage) {
    val wimage = image
    val zBuffer = Array(wimage.height.toInt()) {DoubleArray(wimage.width.toInt()) {Double.MIN_VALUE} }
    val pw = wimage.pixelWriter

    private fun processPixel (p: Vector3, c: Color) {
        val x = p.x.toInt()
        val y = p.y.toInt()
        if (p.z > zBuffer[x][y]) {
            zBuffer[x][y] = p.z
            pw.setColor(x, y, c)
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
            if (v.x > minX) minX = v.x
            if (v.y > minY) minY = v.y
        }
        return IntArray(4) {minX.toInt(); minY.toInt(); maxX.toInt(); maxY.toInt()}
    }
    
    private fun calculateFacetColor(facet: Facet, camera: Camera, screenCenter: Vector2D) : Color {
        val n = facet.getNormal(camera, screenCenter)
        val k = 130.0
        val col = facet.color
        val fraction = n.z
        //TODO: уточнить, не будет ли слишком сильно затемняться
        return Color.rgb((col.red * fraction).toInt(), (col.red * fraction).toInt(), (col.red * fraction).toInt())
    }

    private fun calcBarycentric(x: Int, y: Int, triangle: MutableList<Vector3>, square: Double) : Vector3 {
        val vx = ((y - triangle[2].y) * (triangle[1].x - triangle[2].x) + (triangle[1].y - triangle[2].y) * (triangle[2].x - x)) / square
        val vy = ((y - triangle[0].y) * (triangle[2].x - triangle[0].x) + (triangle[2].y - triangle[0].y) * (triangle[0].x - x)) / square
        val z = 1 - vx - vy
        return Vector3(vx, vy, z)
    }

    private fun baryCentricInterpol (a: Vector3, b: Vector3, c: Vector3, bary: Vector3) : Double {
        return bary.x * a.z + bary.y * b.z + bary.z * c.z
    }

    private fun processFacet(facets: MutableList<Vector3>, rect: IntArray, color: Color) {
        val square = (facets[0].y - facets[2].y) * (facets[1].x - facets[2].x) +
                     (facets[1].y - facets[2].y) * (facets[2].x - facets[0].x)
        for (y in rect[1] .. rect[3]) {
            for (x in rect[0]..rect[2]) {
                val barCoords = calcBarycentric(x, y, facets, square)
                if (barCoords.x >= -1e5 && barCoords.y >= -1e5 && barCoords.z >= -1e5) {
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

    fun renderScene () {
        //TODO: рендер сцены -- processLine
    }
}