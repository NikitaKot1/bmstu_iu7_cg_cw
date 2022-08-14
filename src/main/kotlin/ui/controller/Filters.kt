package com.example.demo.controller

import com.example.demo.view.MainView
import javafx.scene.image.Image
import javafx.scene.image.PixelReader
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import tornadofx.*

class Filters: Controller() {

    private val width = 400
    private val height = 400

    private val img = Image("/drawimage.jpg")
    private val result = WritableImage(img.pixelReader, width, height)
    private val resultWriter = result.pixelWriter

    fun makePixelsDuller(image: WritableImage): WritableImage {
        val pixelReader = image.pixelReader
        val pixelWriter = image.pixelWriter

        // Determine the color of each pixel in a specified row
        for (i in 0 until width) {
            for (j in 0 until 250) {
                val color = pixelReader.getColor(i, j)

                pixelWriter.setColor(i, j, color.desaturate())
            }
        }
        return result
    }

    fun makePixelsBrighter(image: PixelReader): WritableImage {
        // Determine the color of each pixel in a specified row
        for (i in 0 until 500) {
            for (j in 0 until 250) {
                val color = image.getColor(i, j)

                resultWriter.setColor(i, j, color.brighter())
            }
        }
        return result
    }

    /**
     *  From here on down, the filter functions applies to black-and-white
     *  images only because 0 and 1 is easier to calculate operations than
     *  for pixels with RGB values;
     */

    // shift pixels in image up by one place
    fun shiftPixelsUp(image: PixelReader): WritableImage {
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (y < height - 1) {
                    // get the bottom neighbor pixel
                    resultWriter.setColor(x, y, image.getColor(x,y + 1))
                } else {
                    resultWriter.setColor(x, y, Color.WHITE)
                }
            }
        }
        return result
    }

    // shift pixels in image down by one place
    fun shiftPixelsDown(image: PixelReader): WritableImage {
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (y > 0) {
                    // get the bottom neighbor pixel
                    resultWriter.setColor(x, y, image.getColor(x,y - 1))
                } else {
                    resultWriter.setColor(x, y, Color.WHITE)
                }
            }
        }
        return result
    }

    // compare the original image with the shifted pixels image
    fun sideEdge(image: PixelReader) : WritableImage {
        // Determine the color of each pixel in a specified row
        for (x in 0 until width/2) {
            for (y in 0 until height) {
                val temp = image.getColor(x, y)
                val hi = width-1

                resultWriter.setColor(x, y, image.getColor(hi - x, y))
                resultWriter.setColor(hi - x, y, temp)
            }
        }
        return result
    }
}