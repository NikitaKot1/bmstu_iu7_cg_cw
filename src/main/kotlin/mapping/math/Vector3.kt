package mapping.math

import mapping.math.matrixes.Matrix
import kotlin.math.sqrt

class Vector3 (private val xi: Double, private val yi: Double, private val zi: Double, private val wi: Double = 1.0) {
    var x = xi
    var y = yi
    var z = zi
    var w = wi

    operator fun plus(b: Vector3): Vector3 {
        return Vector3(x + b.x, y + b.y, z + b.z)
    }

    operator fun minus(b: Vector3): Vector3 {
        return Vector3(x - b.x, y - b.y, z - b.z)
    }

//    operator fun times(b: Vector3): Double {
//        return x * b.x + y * b.y + z * b.z
//    }

    operator fun times(b: Vector3): Vector3 {
        val x = this.y * b.z - this.z * b.y
        val y = this.z * b.x - this.x * b.z
        val z = this.x * b.y - this.y * b.x
        return Vector3(x, y, z)
    }

    operator fun times(f: Double): Vector3 {
        return Vector3(x * f, y * f, z * f)
    }

    operator fun div(b: Vector3): Vector3 {
        return Vector3(x * b.x, y * b.y, z * b.z)
    }

    operator fun get(i: Int): Double {
        return if (i <= 0)
            x
        else if (i == 1)
            y
        else if (i == 2)
            z
        else
            w
    }

    public fun norm(): Double {
        return sqrt(x * x + y * y + z * z)
    }

    public fun normalize(l : Double) : Vector3 {
        return this * (l / norm())
    }

    public fun trasform(matrix: Matrix) {
        val resolt = arrayOfNulls<Double>(4)
        val data = arrayOf(x, y, z, w)

        for (i in 0..3)
            for (j in 0..3)
                resolt[i] = resolt[i]?.plus(data[j] * matrix.matrix[i][j])

        x = resolt[0]!!
        y = resolt[1]!!
        z = resolt[2]!!
    }

    operator fun unaryMinus(): Vector3 {
        return Vector3(-x, -y, -z)
    }
}