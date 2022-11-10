package mapping.fileload

import mapping.math.Vector3
import mapping.objects.camera.Camera
import mapping.objects.model.Model
import tornadofx.Vector2D
import java.io.File
import kotlin.math.max

class Saver (fileName: String) {
    private val file = File(fileName)

    fun createFile() {
        val isNew : Boolean = file.createNewFile()
        if (isNew)
            println("Created")
        else
            println("already")
    }

    fun closeFile() {

    }

    fun saveModel(model: Model, camera: Camera) {
        var writedString = ""
        val newV = mutableListOf<Vector3>()
        for (v in model.poligons.vertices) {
            newV += v.getScreenPos(camera, Vector2D(.0, .0))
        }

        var maxX = .0
        var maxY = .0
        var maxZ = .0
        var minX = 10000.0
        var minY = 10000.0
        var minZ = 10000.0
        for (v in newV) {
            if (maxX < v.x) maxX = v.x
            if (maxY < v.y) maxY = v.y
            if (maxZ < v.z) maxZ = v.z

            if (minX > v.x) minX = v.x
            if (minY > v.y) minY = v.y
            if (minZ > v.z) minZ = v.z
        }

        val centerX = (maxX - minX) / 2
        val centerY = (maxY - minY) / 2
        val centerZ = (maxZ - minZ) / 2
        val del = max(centerX, max(centerY, centerZ))

        for (v in newV) {
            v.x -= centerX
            v.x /= del
            v.y -= centerY
            v.y /= del
            v.z -= centerZ
            v.z /= del
        }

        writedString += "o castom model\n"
       // file.writeText("o castom model\n")

        for (v in newV) {
            val x = v.x
            val y = v.y
            val z = v.z
            writedString += "v $x $y $z\n"
        }

        for (f in model.poligons.facets) {
            val vr = Array(3) {-1}
            for (k in 0..2) {
                for (v in 0 until model.poligons.vertices.size)
                    if (f.dots[k] === model.poligons.vertices[v])
                        vr[k] = v
            }
            val v1 = vr[0] + 1
            val v2 = vr[1] + 1
            val v3 = vr[2] + 1
            val c1 = (f.color.red * 255).toInt()
            val c2 = (f.color.green * 255).toInt()
            val c3 = (f.color.blue * 255).toInt()
            writedString += "f $v1 $v2 $v3 $c1 $c2 $c3\n"
        }
        file.writeText(writedString)
    }
}