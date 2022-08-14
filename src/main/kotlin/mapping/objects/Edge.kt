package mapping.objects

data class Edge (private val p1: Int, private val p2: Int) {
    public var id_p1: Int = -1
    public var id_p2: Int = -1
    init {
        id_p1 = p1
        id_p2 = p2
    }
}