package mapping.math.matrixes

class MoveMatrix(private val x: Double, private val y: Double, private val z: Double) : Matrix() {
    init {
        matrix[0][0] = 1.0
        matrix[1][1] = 1.0
        matrix[2][2] = 1.0
        matrix[4][4] = 1.0

        matrix[0][3] = x
        matrix[1][3] = y
        matrix[2][3] = z
    }
}