package mapping.selection

import mapping.objects.model.Model
import mapping.scene.Scene
import tornadofx.Vector2D

class SelectManager {
    //TODO: сделать выбор грани/полигона/точки
    var selectedModel: Model? = null

    fun selectModel(scene: Scene, moudePos: Vector2D) {
        selectedModel = scene.models[0]
        //TODO: ну и модельку все же от позиции делать
    }
}