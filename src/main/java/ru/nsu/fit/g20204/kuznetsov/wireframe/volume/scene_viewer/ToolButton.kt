package ru.nsu.fit.g20204.kuznetsov.wireframe.volume.scene_viewer

import java.awt.Dimension
import java.awt.Image
import java.io.IOException
import java.util.logging.Logger
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JButton

class ToolButton(
    val imagePath: String,
    val tip: String
) : JButton() {
    private var name: String = tip
    private var logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)

    init {
        isFocusPainted = false
        val stream = javaClass.classLoader.getResourceAsStream(imagePath)
        val width = 30
        val height = 25
        try {
            val img = ImageIO.read(stream)
            val icon = ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH))
            setIcon(icon)
        } catch (e: IllegalArgumentException) {
            logger.warning("Bad resource path")
        } catch (e: IOException) {
            logger.warning("Bad resource path")
        }
        toolTipText = tip
        maximumSize = Dimension(width, height)
    }
}