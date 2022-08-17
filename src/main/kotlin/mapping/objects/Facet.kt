package mapping.objects

import javafx.scene.paint.Color

data class Facet (val dotAr: Array<Dot>, val edgeAr: Array<Edge>, val startColor: Color = Color.rgb(0, 0, 0)) {
    public var dots = dotAr
    private val edges = edgeAr
    public var equation = arrayOfNulls<Double>(4)
    public var color: Color = startColor

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