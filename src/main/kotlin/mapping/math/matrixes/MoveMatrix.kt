package mapping.math.matrixes

class MoveMatrix(private val x: Double, private val y: Double, private val z: Double) : Matrix() {
    init {
        matrix[0][0] = 1.0
        matrix[1][1] = 1.0
        matrix[2][2] = 1.0
        matrix[3][3] = 1.0

        matrix[3][0] = x
        matrix[3][1] = y
        matrix[3][2] = z
    }
}