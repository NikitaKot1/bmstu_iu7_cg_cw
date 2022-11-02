package mapping.fileload

import mapping.math.Vector3
import mapping.objects.camera.Camera
import mapping.scene.Scene
import mapping.objects.light.Light
import mapping.objects.model.Model
import mapping.objects.model.parts.Edge
import mapping.objects.model.parts.Facet
import mapping.objects.model.parts.PartsOfModel
import mapping.objects.model.parts.Vertex
import java.io.File

class Loader (fileName: String){
    private var scrFile = File(fileName)

    fun openFile(fileName: String) {
        scrFile = File(fileName)
    }

    fun closeFile() {

    }

    private fun readPosition(line: String) : Vector3 {
        val spl = line.split(' ')
        return Vector3(spl[1].toDouble(), spl[2].toDouble(), spl[3].toDouble())
    }

    private fun readFacetDetails(line: String) : List<Int> {
        val facetVert = mutableListOf<Int>()
        val spl = line.split(' ')
        for (i in 1 until spl.size)
            facetVert.add(i - 1)
        //TODO: проверочку на 3 бы...
        return facetVert
    }

    private fun addEdgeToModel(edges: MutableList<Edge>, newEdge: Edge) : Edge{
        var found = false
        var foundEdge = newEdge
        if (edges.size == 0)
            edges += newEdge
        else {
            for (e in edges) {
                if ((e.id_p1 == newEdge.id_p1 && e.id_p2 == newEdge.id_p2) || (e.id_p2 == newEdge.id_p1 && e.id_p1 == newEdge.id_p2)) {
                    found = true
                    foundEdge = e
                    break
                }
            }
            if (!found)
                edges += newEdge
        }
        return foundEdge
    }


    fun loadModels() : List<Model> {
        val models = mutableListOf<Model>()
        var details : PartsOfModel? = null
        var verticesRead = 0
        val edges = mutableListOf<Edge>()

        val lineList = mutableListOf<String>()
        scrFile.useLines { lines -> lines.forEach { lineList.add(it) } }

        for (line in lineList) {
            val key = line.split(' ')[0]
            if (key == "o") {
                if (details != null) {
                    details.edges = edges
                    details.center = details.findArithCenter()
                    val m = Model(details)
                    models += m

                    verticesRead += details.vertices.size
                }
                details = PartsOfModel()
            }
            else if (key == "v") {
                val v = readPosition(line)
                details?.vertices?.add(Vertex(v))
            }
            else if (key == "f") {
                val vNumbers = readFacetDetails(line)
                val vertices = mutableListOf<Vertex>()
                val facetEdges = mutableListOf<Edge>()

                for (num in vNumbers) if (details != null) {
                    vertices += details.vertices[num - verticesRead]
                }
                for (i in 0 until vertices.size) {
                    var e = Edge(vertices[i], vertices[(i+1)%vertices.size])
                    e = addEdgeToModel(edges, e)
                    facetEdges += e
                }
                val f = Facet(vertices, facetEdges)
                details?.facets?.plusAssign(f)
            }
        }

        if (details?.toString().toBoolean()) {
            details?.edges = edges
            details?.center = details?.findArithCenter()!!
            val m = Model(details)
            models += m
            verticesRead += details.vertices.size
        }

        return models
    }

    fun loadScene() : Scene {
        val models = mutableListOf<Model>()
        var details : PartsOfModel? = null
        var verticesRead = 0
        val edges = mutableListOf<Edge>()
        var light : Light? = null
        var camera : Camera? = null

        val lineList = mutableListOf<String>()
        scrFile.useLines { lines -> lines.forEach { lineList.add(it) } }

        for (line in lineList) {
            val key = line.split(' ')[0]
            if (key == "o") {
                if (details != null) {
                    details.edges = edges
                    details.center = details.findArithCenter()
                    val m = Model(details)
                    models += m

                    verticesRead += details.vertices.size
                }
                details = PartsOfModel()
            }
            else if (key == "v") {
                val v = readPosition(line)
                details?.vertices?.add(Vertex(v))
            }
            else if (key == "f") {
                val vNumbers = readFacetDetails(line)
                val vertices = mutableListOf<Vertex>()
                val facetEdges = mutableListOf<Edge>()

                for (num in vNumbers) if (details != null) {
                    vertices += details.vertices[num - verticesRead]
                }
                for (i in 0 until vertices.size) {
                    var e = Edge(vertices[i], vertices[(i+1)%vertices.size])
                    e = addEdgeToModel(edges, e)
                    facetEdges += e
                }
                val f = Facet(vertices, facetEdges)
                //TODO: цвет?
                details?.facets?.plusAssign(f)
            }
            else if (key == "ls") {
                val v = Vertex(readPosition(line))
                light = Light(v)
            }
            else if (key == "cam") {
                val v = Vertex(readPosition(line))
                camera = Camera(v)
            }
        }

        if (details?.toString().toBoolean()) {
            details?.edges = edges
            details?.center = details?.findArithCenter()!!
            val m = Model(details)
            models += m
            verticesRead += details.vertices.size
        }

        val scene = Scene()
        scene.models = models
        if (light != null) {
            scene.lights = light
        }
        if (camera != null) {
            scene.camera = camera
        }
        return scene
    }
}