package mapping.math.matrixes

import kotlin.math.cos
import kotlin.math.sin

class RotateOXMatrix(private val angle: Double) : Matrix() {
    init {
        matrix[0][0] = 1.0
        matrix[1][1] = cos(angle)
        matrix[1][2] = -sin(angle)
        matrix[2][1] = sin(angle)
        matrix[2][2] = cos(angle)
        matrix[3][3] = 1.0
    }
}