package mapping.objects.camera

import mapping.math.Vector3
import mapping.objects.SimpleObject
import mapping.objects.model.parts.Vertex

class Camera (private val v: Vertex) : SimpleObject() {
    var pos = v
    lateinit var center: Vertex
}