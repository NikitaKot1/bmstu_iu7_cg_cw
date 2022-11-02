package mapping.fileload

import mapping.objects.model.Model
import mapping.scene.Scene

class FileManager {
    fun loadScene(fileName: String) : Scene {
        val loader = Loader(fileName)
        val scene = loader.loadScene()
        loader.closeFile()
        return scene
    }

    fun loadModels(fileName: String) : List<Model> {
        val loader = Loader(fileName)
        val models = loader.loadModels()
        loader.closeFile()
        return models
    }

    fun saveScene() {
        //TODO: сам понимаешь, надо сделать когда-нибудь
    }
}