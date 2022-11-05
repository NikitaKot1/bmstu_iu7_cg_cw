package mapping.coord

import tornadofx.Vector3D

object Coord {
    fun fromOtnToAbs (x: Double, y: Double, z:Double, mul: Double) : Vector3D {
        val xn = mul + x * mul
        val yn = mul + y * mul
        val zn = mul + z * mul
        return Vector3D(xn, yn, zn)
    }

    fun fromAbsToOtn (x: Int, y: Int, z:Int, mul: Double) : Vector3D {
        val xn = x / mul - 1
        val yn = y / mul - 1
        val zn = z / mul - 1
        return Vector3D(xn, yn, zn)
    }
}