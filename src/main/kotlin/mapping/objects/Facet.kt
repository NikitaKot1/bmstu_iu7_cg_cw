package mapping.objects

import javafx.scene.paint.Color

class Facet {
    private val dots = emptyArray<Dot>()
    private val edges = emptyArray<Edge>()
    private val equation = arrayOfNulls<Int>(4)
    private val color: Color = Color.rgb(0, 0, 0)
}