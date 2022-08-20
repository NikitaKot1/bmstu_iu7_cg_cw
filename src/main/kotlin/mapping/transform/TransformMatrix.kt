package mapping.transform

import mapping.objects.Dot
import kotlin.math.cos
import kotlin.math.sin

class TransformMatrix () : Matrix () {
    //public val matrix :Array<IntArray> =
    public val devMatrix : Array<DoubleArray> = Array(4) {DoubleArray(4) {0.0} }

    init {
        devMatrix[0][0] = 1.0
        devMatrix[1][1] = 1.0
        devMatrix[2][2] = 1.0
        devMatrix[3][3] = 1.0
    }

    public fun move (moveParam: Dot) {
        devMatrix[3][0] = moveParam.xi
        devMatrix[3][1] = moveParam.yi
        devMatrix[3][2] = moveParam.zi
        this.mul(devMatrix)
    }

    public fun scale (scaleParam: Dot) {
        devMatrix[0][0] = scaleParam.xi
        devMatrix[1][1] = scaleParam.yi
        devMatrix[2][2] = scaleParam.zi
        this.mul(devMatrix)
    }

    public fun rotate (rotateParam: Dot) {
        //plz no
        //OX
        devMatrix[1][1] = cos(rotateParam.xi)
        devMatrix[1][2] = sin(rotateParam.xi)
        devMatrix[2][1] = -sin(rotateParam.xi)
        devMatrix[2][2] = cos(rotateParam.xi)
        this.mul(devMatrix)
        devMatrix[1][1] = 1.0
        devMatrix[1][2] = 1.0
        devMatrix[2][1] = 1.0
        devMatrix[2][2] = 1.0

        //OY
        devMatrix[0][0] = cos(rotateParam.yi)
        devMatrix[0][2] = sin(rotateParam.yi)
        devMatrix[2][0] = -sin(rotateParam.yi)
        devMatrix[2][2] = cos(rotateParam.yi)
        this.mul(devMatrix)
        devMatrix[0][0] = 1.0
        devMatrix[0][2] = 1.0
        devMatrix[2][0] = 1.0
        devMatrix[2][2] = 1.0

        //OZ
        devMatrix[0][0] = cos(rotateParam.zi)
        devMatrix[0][1] = sin(rotateParam.zi)
        devMatrix[1][0] = -sin(rotateParam.zi)
        devMatrix[1][1] = cos(rotateParam.zi)
        this.mul(devMatrix)
    }
}
