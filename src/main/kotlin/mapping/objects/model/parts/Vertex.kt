package mapping.objects.model.parts

import mapping.math.Vector3
import mapping.math.matrixes.Matrix
import mapping.math.matrixes.MoveMatrix
import mapping.math.matrixes.RotateOXMatrix
import mapping.math.matrixes.RotateOYMatrix
import mapping.math.matrixes.RotateOZMatrix
import mapping.math.matrixes.ScaleMatrix
import mapping.objects.camera.Camera
import tornadofx.Vector2D

class Vertex (pos: Vector3){
    var position = pos
    var transmatr = Matrix()
    var selected = false
    fun getNewPosition() : Vector3 {
        return transmatr * position
    }

    fun getScreenPos(camera: Camera, screenCenter: Vector2D) : Vector3 {
        //TODO: вообще в ТЗ написано, чтоб можно было именно камеру двигать, над этим надо подумать
        //TODO: в теории, просто матрицу vertex помножить на матрицу камеры перед соотв изменением

        val after = transmatr * position
        return Vector3(after.x / after.w, after.y / after.w,after.z / after.w)
    }

    fun move(dif: Vector3) {
        transmatr *= MoveMatrix(dif.x, dif.y, dif.z)
    }
    fun scale(k: Vector3) {
        transmatr *= ScaleMatrix(k.x, k.y, k.z)
    }
    fun rotate(angels: Vector3) {
        rotateOX(angels.x)
        rotateOY(angels.y)
        rotateOZ(angels.z)
    }

    private fun rotateOX(angel: Double) {
        transmatr *= RotateOXMatrix(angel)
    }
    private fun rotateOY(angel: Double) {
        transmatr *= RotateOYMatrix(angel)
    }
    private fun rotateOZ(angel: Double) {
        transmatr *= RotateOZMatrix(angel)
    }

    //дистанция?
}