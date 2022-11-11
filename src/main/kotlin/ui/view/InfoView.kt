package ui.view

import tornadofx.View
import tornadofx.label

class InfoView(warn: String) : View("Dot pos:") {
    override val root = label {
        text = warn
    }
}