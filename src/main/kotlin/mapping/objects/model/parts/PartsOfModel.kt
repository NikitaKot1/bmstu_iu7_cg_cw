package mapping.objects.model.parts

import mapping.math.Vector3
import mapping.math.Vertex

class PartsOfModel {
    var vertices = mutableListOf<Vertex>()
    var edges = mutableListOf<Edge>()
    var facets = mutableListOf<Facet>()
    var center = Vector3(.0, .0, .0, 1.0)

    private fun move(movep: Vector3) {
        for (v in vertices) {

        }
    }
    private fun scale(scalep: Vector3) {

    }
    private fun trans(transp: Vector3) {

    }


}