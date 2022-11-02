package mapping.objects.model.parts

data class Edge (private val p1: Vertex, private val p2: Vertex) {
    public var id_p1 = p1
    public var id_p2 = p2
}