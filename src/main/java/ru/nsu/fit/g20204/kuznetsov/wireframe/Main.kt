package ru.nsu.fit.g20204.kuznetsov.wireframe

import ru.nsu.fit.g20204.kuznetsov.wireframe.model.Geometry
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.CameraNode
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.ModelNode
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.SceneNode
import ru.nsu.fit.g20204.kuznetsov.wireframe.scene_viewer.ViewerFrame
import javax.swing.WindowConstants

object Main {
    var modelNode: ModelNode? = null
    var camera: CameraNode? = null
    @JvmStatic
    fun main(args: Array<String>) {
        run()
    }

    private fun run() {
        val scene = createScene()
        val sceneViewer = ViewerFrame(scene)
        sceneViewer.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    }

    private fun createScene(): SceneNode {
        val scene = SceneNode(null)
        modelNode = ModelNode(scene)
        scene.addChild(modelNode)
        scene.model = modelNode
        modelNode.model = Geometry()
        camera = scene.createCameraNode()
        camera.translate(0.0, 0.0, -5.0)
        return scene
    }
}