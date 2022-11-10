package mapping.fileload

import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

object Chooser {
    val chooser = JFileChooser()
    val filter = FileNameExtensionFilter("object files", "obj", "txt", "sol")

    fun openFile() : String {
        chooser.fileFilter = filter
        val returnVal = chooser.showOpenDialog(null)
        if (returnVal == JFileChooser.APPROVE_OPTION)
            println(chooser.selectedFile.path)
        return chooser.selectedFile.path
    }

    fun saveFile() : String {
        chooser.fileFilter = filter
        val returnVal = chooser.showSaveDialog(null)
        if (returnVal == JFileChooser.APPROVE_OPTION)
            println(chooser.selectedFile.path)
        return chooser.selectedFile.path
    }
}