package ru.nsu.fit.g20204.kuznetsov.wireframe.scene_viewer

import ru.nsu.fit.g20204.kuznetsov.wireframe.node.SceneNode
import ru.nsu.fit.g20204.kuznetsov.wireframe.util.ModelParser
import java.awt.FileDialog
import java.io.File
import javax.swing.JFrame
import javax.swing.JOptionPane

class FileChooser(private val parentFrame: JFrame) {
    fun showOpenDialog(): SceneNode? {
        val fileDialog = FileDialog(parentFrame, "Open scene", FileDialog.LOAD)
        fileDialog.file = "*.graphics"
        fileDialog.isVisible = true
        return if (fileDialog.file != null) {
            ModelParser.fileToScene(fileDialog.files[0])
        } else null
    }

    fun showSaveDialog(scene: SceneNode) {
        val fileDialog = FileDialog(parentFrame, "Save scene", FileDialog.SAVE)
        fileDialog.file = "*.graphics"
        fileDialog.isVisible = true
        if (fileDialog.file != null) {
            var file = fileDialog.files[0]

            // Add extension if needed
            if (!file.name.endsWith(".graphics")) file = File(file.path + ".graphics")
            val saved: Boolean = ModelParser.sceneToFile(scene, file)
            if (!saved) {
                JOptionPane.showMessageDialog(parentFrame, "Failed to save file!")
            }
        }
    }
}
