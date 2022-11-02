package mapping.objects.model

import mapping.math.Vector3
import mapping.objects.model.parts.PartsOfModel


class Model (parts: PartsOfModel) {
    val poligons = parts

    fun transform(movep: Vector3, scalep: Vector3, rotatep: Vector3) {
        poligons.transform(movep, scalep, rotatep)
    }
}