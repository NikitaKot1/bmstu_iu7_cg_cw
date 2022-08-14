package mapping.objects

import javafx.scene.paint.Color

data class Facet (val dotAr: Array<Dot>, val edgeAr: Array<Edge>, val startColor: Color = Color.rgb(0, 0, 0)) {
    private val dots = dotAr
    private val edges = edgeAr
    private var equation = arrayOfNulls<Double>(4)
    private var color: Color = Color.rgb(0, 0, 0)

    init {
        color = startColor
        //calculate facet's equation
        val x0: Double = dots[0].xi
        val y0: Double = dots[0].yi
        val z0: Double = dots[0].zi

        val x1: Double = dots[1].xi
        val y1: Double = dots[1].yi
        val z1: Double = dots[1].zi

        val x2: Double = dots[2].xi
        val y2: Double = dots[2].yi
        val z2: Double = dots[2].zi

        val a: Double = (y1 - y0) * (z2 - z0) - (y2 - y0) * (z1 - z0)
        val b: Double = (z1 - z0) * (x2 - x0) - (z2 - z0) * (x1 - x0)
        val c: Double = (x1 - x0) * (y2 - y0) - (x2 - x0) * (y1 - y0)
        val d: Double = -1 * (a * x0 + b * y0 + c * z0)

        equation[0] = a
        equation[1] = b
        equation[2] = c
        equation[3] = d
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Facet

        if (!dotAr.contentEquals(other.dotAr)) return false
        if (!dots.contentEquals(other.dots)) return false
        if (!edges.contentEquals(other.edges)) return false
        if (!equation.contentEquals(other.equation)) return false
        if (color != other.color) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dotAr.contentHashCode()
        result = 31 * result + dots.contentHashCode()
        result = 31 * result + edges.contentHashCode()
        result = 31 * result + equation.contentHashCode()
        result = 31 * result + color.hashCode()
        return result
    }
}