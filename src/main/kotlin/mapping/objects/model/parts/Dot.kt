package mapping.objects.model.parts

data class Dot (private val x: Double, private val y: Double, private val z: Double) {
    public var xi: Double = 0.0
    public var yi: Double = 0.0
    public var zi: Double = 0.0
    init {
        xi = x
        yi = y
        zi = z
    }
}