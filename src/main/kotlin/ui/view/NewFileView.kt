package ui.view

import javafx.geometry.Pos
import mapping.fileload.Chooser
import mapping.fileload.FileManager
import mapping.scene.Scene
import tornadofx.*

class NewFileView (scene1: Scene, fileManager: FileManager) : View(""){


    override val root = vbox {
        label { text = "Сохранить изменения?" }
        hbox {
            alignment = Pos.CENTER
            button ("   Да   ").action {
                val fln = Chooser.saveFile()
                if (fln == "0")
                    WarningView("Ошибка при выборе файла!").openWindow()
                else
                    fileManager.saveModel(fln, scene1.camera, scene1.models[0])
            }
            button ("   Нет  ").action {
                close()
            }
        }
    }
}