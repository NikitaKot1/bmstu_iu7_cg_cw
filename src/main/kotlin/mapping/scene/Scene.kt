package mapping.scene

import mapping.objects.camera.Camera
import mapping.objects.light.Light
import mapping.objects.model.Model

class Scene {
    val models = mutableListOf<Model>()
    lateinit var lights : Light
    lateinit var camera : Camera
}