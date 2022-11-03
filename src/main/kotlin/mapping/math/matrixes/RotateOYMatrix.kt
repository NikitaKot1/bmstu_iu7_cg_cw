package mapping.math.matrixes

import kotlin.math.cos
import kotlin.math.sin

class RotateOYMatrix(private val angle: Double) : Matrix() {
    init {
        matrix[0][0] = cos(angle)
        matrix[1][1] = 1.0
        matrix[2][0] = -sin(angle)
        matrix[0][2] = sin(angle)
        matrix[2][2] = cos(angle)
        matrix[3][3] = 1.0
    }
}