package ui.app

import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
    }

    init {
        label and heading {
            padding = box(40.px)
            fontSize = 30.px
            fontWeight = FontWeight.BOLD

        }
        label {
            fontSize = 16.px
        }
        button {
            padding = box(15.px)
            fontSize = 15.px
            fontWeight = FontWeight.BOLD
        }
//        toggleButton {
//            fontSize = 12.px
//        }
//        checkBox {
//            fontSize = 12.px
//        }
        s(toggleButton, checkBox, textField) {
            fontSize = 15.px
        }
    }
}