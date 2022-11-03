package mapping.math.matrixes

import mapping.math.Vector3

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
            result.matrix[i][i] = 0.0
        for (i in 0..3)
            for (j in 0..3)
                for (k in 0..3)
                    result.matrix[i][j] += matrix[i][k] * matr.matrix[k][j]
        return result
    }

    operator fun times(ver: Vector3) : Vector3 {
        val result = Vector3(.0, .0, .0, .0)
        result.x = ver.x * matrix[0][0] + ver.y * matrix[1][0] + ver.z * matrix[2][0] + ver.w * matrix[3][0]
        result.y = ver.x * matrix[0][1] + ver.y * matrix[1][1] + ver.z * matrix[2][1] + ver.w * matrix[3][1]
        result.z = ver.x * matrix[0][2] + ver.y * matrix[1][2] + ver.z * matrix[2][2] + ver.w * matrix[3][2]
        result.w = ver.x * matrix[0][3] + ver.y * matrix[1][3] + ver.z * matrix[2][3] + ver.w * matrix[3][3]
        //result.w = 1.0 //TODO:!!!
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