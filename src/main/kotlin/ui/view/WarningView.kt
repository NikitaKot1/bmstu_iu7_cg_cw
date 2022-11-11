package ui.view

import tornadofx.View
import tornadofx.label

class WarningView(warn: String) : View("") {
    override val root = label {
        text = warn
    }
}