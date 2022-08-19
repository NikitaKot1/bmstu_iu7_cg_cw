package mapping.objects

import javafx.scene.paint.Color

class ZBufferString (val width: Int){
    var zbuffer = arrayOfNulls<Double>(width)
    var colorbuffer = arrayOfNulls<Color>(width)
    var eqbuffer = arrayOfNulls<Array<Double>>(width)

    public fun fill() {
        zbuffer.fill(-Double.MAX_VALUE)
        colorbuffer.fill(Color.AQUAMARINE)
    }
}