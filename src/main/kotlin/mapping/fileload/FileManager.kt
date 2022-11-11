package mapping.fileload

import mapping.objects.camera.Camera
import mapping.objects.model.Model
import mapping.scene.Scene

class FileManager {
    fun loadScene(fileName: String, mul: Double) : Scene {
        val loader = Loader(fileName)
        val scene = loader.loadScene(mul)
        loader.closeFile()
        return scene
    }

    fun loadModels(fileName: String, mul: Double) : List<Model> {
        val loader = Loader(fileName)
        val models = loader.loadModels(mul)
        loader.closeFile()
        return models
    }

    fun saveModel(fileName: String, camera: Camera, model: Model) {
        val saver = Saver(fileName)
        saver.createFile()
        saver.saveModel(model, camera)
        saver.closeFile()
    }
}