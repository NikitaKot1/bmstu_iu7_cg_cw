package mapping.objects.model.parts

import mapping.math.Vector3

class PartsOfModel {
    var vertices = mutableListOf<Vertex>()
    var edges = mutableListOf<Edge>()
    var facets = mutableListOf<Facet>()
    var center = Vertex(Vector3(.0, .0, .0, 1.0))

    private fun move(movep: Vector3) {
        for (v in vertices)
            v.move(movep)
    }
    private fun scale(scalep: Vector3) {
        for (v in vertices)
            v.scale(scalep)
    }
    private fun rotate(rotatep: Vector3) {
        for (v in vertices)
            v.rotate(rotatep)
    }

    fun transform(movep: Vector3, scalep: Vector3, rotatep: Vector3) {
        move(-center.getNewPosition())
        scale(scalep)
        rotate(rotatep)
        move(movep)
        move(center.getNewPosition())
    }

    fun findArithCenter() : Vector3 {
        if (vertices.size == 0)
            return Vector3(.0, .0, .0)
        var point = vertices[0].getNewPosition()
        var maxX = point.x
        var minX = maxX
        var maxY = point.y
        var minY = maxY
        var maxZ = point.z
        var minZ = maxZ
        for (v in vertices) {
            point = v.getNewPosition()
            if (point.x > maxX) maxX = point.x
            if (point.x < minX) minX = point.x
            if (point.y > maxY) maxY = point.x
            if (point.y < minY) minY = point.x
            if (point.z > maxZ) maxZ = point.x
            if (point.z < minZ) minZ = point.x
        }
        return Vector3((maxX + minX) / 2, (maxY + minY) / 2,(maxZ + minZ) / 2)
    }
}