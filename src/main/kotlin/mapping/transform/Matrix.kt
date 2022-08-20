package mapping.transform

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
}