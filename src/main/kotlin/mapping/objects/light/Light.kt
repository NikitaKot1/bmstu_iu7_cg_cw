package mapping.objects.light

import mapping.math.Vector3
import mapping.objects.SimpleObject

class Light (private val v: Vector3) : SimpleObject() {
    var pos = v
    lateinit var center: Vector3
}