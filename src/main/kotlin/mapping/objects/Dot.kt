package mapping.objects

data class Dot (val x: Double, val y: Double, val z: Double) {
    public var xi: Double = 0.0
    public var yi: Double = 0.0
    public var zi: Double = 0.0
    init {
        xi = x
        yi = y
        zi = z
    }
}