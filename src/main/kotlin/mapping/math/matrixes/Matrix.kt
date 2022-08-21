package mapping.math.matrixes

open class Matrix {
    public var matrix : Array<DoubleArray> = Array(4) {DoubleArray(4) {0.0} }

    init {
        matrix[0][0] = 1.0
        matrix[1][1] = 1.0
        matrix[2][2] = 1.0
        matrix[3][3] = 1.0
    }

    public fun mul (otherMatrix: Array<DoubleArray>) : Array<DoubleArray> {
        val rezMatrix : Array<DoubleArray> = Array(4) {DoubleArray(4) {0.0} }
        for (i in 0..3)
            for (j in 0..3)
                for (k in 0..3)
                    rezMatrix[i][j] += matrix[i][k] * otherMatrix[k][j]
        return rezMatrix
    }

    operator fun times(matr: Matrix) : Matrix {
        val result = Matrix()
        for (i in 0..3)
            for (j in 0..3)
                for (k in 0..3)
                    result.matrix[i][j] += matrix[i][k] * matr.matrix[k][j]
        return result
    }

    public fun transpose() : Matrix {
        val result = Matrix()
        for (i in 0..3)
            for (j in 0..3)
                result.matrix[j][i] = matrix[i][j]
        return result
    }
}