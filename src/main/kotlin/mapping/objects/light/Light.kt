package mapping.objects.light

import mapping.objects.SimpleObject
import mapping.objects.model.parts.Vertex

class Light (private val v: Vertex) : SimpleObject() {
    var pos = v
    lateinit var center: Vertex
}