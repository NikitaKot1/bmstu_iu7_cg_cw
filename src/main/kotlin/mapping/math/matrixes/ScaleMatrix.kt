package mapping.math.matrixes

class ScaleMatrix(private val x: Double, private val y: Double, private val z: Double) : Matrix() {
    init {
        matrix[0][0] = x
        matrix[1][1] = y
        matrix[2][2] = z
        matrix[4][4] = 1.0
    }
}