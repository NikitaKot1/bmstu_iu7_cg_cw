package mapping.objects.camera

import mapping.math.Vector3
import mapping.objects.SimpleObject

class Camera (private val v: Vector3) : SimpleObject() {
    var pos = v
    lateinit var center: Vector3
}