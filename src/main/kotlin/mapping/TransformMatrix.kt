package mapping

import mapping.objects.Dot

class TransformMatrix () {
    //public val matrix :Array<IntArray> =
    public val matrix :Array<DoubleArray> = Array(4) {DoubleArray(4) {0.0} }

    init {
        matrix[0][0] = 1.0
        matrix[1][1] = 1.0
        matrix[2][2] = 1.0
        matrix[3][3] = 1.0
    }

    public fun move (moveParam: Dot) {
        matrix[3][0] += moveParam.xi
        matrix[3][1] += moveParam.yi
        matrix[3][2] += moveParam.zi
    }

    public fun scale (scaleParam: Dot) {
        matrix[0][0] *= scaleParam.xi
        matrix[1][1] *= scaleParam.yi
        matrix[2][2] *= scaleParam.zi
    }

    public fun rotate (rotateParam: Dot) {
        //plz no
    }
}
