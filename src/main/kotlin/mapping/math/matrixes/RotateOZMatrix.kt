package mapping.math.matrixes

import kotlin.math.cos
import kotlin.math.sin

class RotateOZMatrix(private val angle: Double) : Matrix() {
    init {
        matrix[0][0] = cos(angle)
        matrix[0][1] = -sin(angle)
        matrix[1][0] = sin(angle)
        matrix[1][1] = sin(angle)
        matrix[2][2] = 1.0
        matrix[3][3] = 1.0
    }
}