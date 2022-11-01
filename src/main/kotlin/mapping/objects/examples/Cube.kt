package mapping.objects.examples

import javafx.scene.paint.Color
import mapping.objects.model.parts.Dot
import mapping.objects.model.parts.Edge
import mapping.objects.model.parts.Facet
import mapping.objects.SimpleObject

class Cube : SimpleObject() {

    init {
        val dots = arrayOf(Dot(10.0, 10.0, 10.0), Dot(110.0, 10.0, 10.0), Dot(110.0, 110.0, 10.0), Dot(10.0, 110.0, 10.0))
        val edges = arrayOf(Edge(0, 1), Edge(1, 2), Edge(2, 3), Edge(3, 0))
        facets += Facet(dots, edges, Color.RED)

        val dots2 = arrayOf(Dot(10.0, 10.0, 20.0), Dot(110.0, 10.0, 5.0), Dot(110.0, 110.0, 7.0), Dot(10.0, 110.0, 5.0))
        val edges2 = arrayOf(Edge(0, 1), Edge(1, 2), Edge(2, 3), Edge(3, 0))
        facets += Facet(dots2, edges2, Color.GOLD)

        val dots3 = arrayOf(Dot(5.0, 5.0, 5.0), Dot(110.0, 5.0, 8.0), Dot(110.0, 7.0, 0.0), Dot(10.0, 20.0, 10.0))
        val edges3 = arrayOf(Edge(0, 1), Edge(1, 2), Edge(2, 3), Edge(3, 0))
        facets += Facet(dots3, edges3, Color.GREEN)
    }
}