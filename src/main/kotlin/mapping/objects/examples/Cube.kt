package mapping.objects.examples

import javafx.scene.paint.Color
import mapping.objects.Dot
import mapping.objects.Edge
import mapping.objects.Facet
import mapping.objects.SimpleObject

class Cube : SimpleObject() {

    init {
        val dots = arrayOf(Dot(10.0, 10.0, 10.0), Dot(110.0, 10.0, 10.0), Dot(110.0, 110.0, 10.0), Dot(10.0, 110.0, 10.0))
        val edges = arrayOf(Edge(0, 1), Edge(1, 2), Edge(2, 3), Edge(3, 0))
        facets += Facet(dots, edges, Color.RED)
    }
}