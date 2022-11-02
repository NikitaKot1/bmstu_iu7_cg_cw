package mapping.objects.model.parts

import javafx.scene.paint.Color
import mapping.math.Vector3
import mapping.objects.camera.Camera
import tornadofx.Vector2D

data class Facet(val dotAr: List<Vertex>, val edgeAr: List<Edge>, val startColor: Color = Color.rgb(0, 0, 0)) {

    public var dots = dotAr.toMutableList()
    private val edges = edgeAr.toMutableList()
    public var color: Color = startColor

    fun clone(): Facet {
        val newDots = mutableListOf<Vertex>()
        for (d in dots) {
            newDots += Vertex(Vector3(d.position.x, d.position.y, d.position.z))
        }

        val newEdges = mutableListOf<Edge>()
        for (e in edges) {
            newEdges += Edge(Vertex(e.id_p1.position), Vertex(e.id_p2.position))
        }
        return Facet(newDots, newEdges, color)
    }

    fun getNormal(camera: Camera, screenCenter: Vector2D) : Vector3{
        val u = dots[1].getScreenPos(camera, screenCenter) - dots[0].getScreenPos(camera, screenCenter)
        val v = dots[2].getScreenPos(camera, screenCenter) - dots[0].getScreenPos(camera, screenCenter)
        val norm = u * v
        norm.normalize(1.0)
        return norm
    }

}