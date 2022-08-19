package mapping

import mapping.objects.SimpleObject
import mapping.objects.Dot

object Transformator {
    public fun transform (obj: SimpleObject, matr: TransformMatrix): SimpleObject {
        val newObj: SimpleObject = obj
        for (ifacet in 0 until newObj.facets.size){
            val facet = newObj.facets[ifacet]
            for (idot in 0 until facet.dotAr.size) {
                var dot = facet.dotAr[idot]
                val x = (dot.xi * matr.matrix[0][0] + dot.yi * matr.matrix[1][0] + dot.zi * matr.matrix[2][0] + matr.matrix[3][0])
                val y = (dot.xi * matr.matrix[0][1] + dot.yi * matr.matrix[1][1] + dot.zi * matr.matrix[2][1] + matr.matrix[3][1])
                val z = (dot.xi * matr.matrix[0][2] + dot.yi * matr.matrix[1][2] + dot.zi * matr.matrix[2][2] + matr.matrix[3][2])
                dot = Dot(x, y, z)
                newObj.facets[ifacet].dotAr[idot] = dot
            }
        }
        return newObj
    }
}