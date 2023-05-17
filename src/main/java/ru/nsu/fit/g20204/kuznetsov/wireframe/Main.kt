package ru.nsu.fit.g20204.kuznetsov.wireframe

import ru.nsu.fit.g20204.kuznetsov.wireframe.util.model.Geometry
import ru.nsu.fit.g20204.kuznetsov.wireframe.volume.node.CameraNode
import ru.nsu.fit.g20204.kuznetsov.wireframe.volume.node.ModelNode
import ru.nsu.fit.g20204.kuznetsov.wireframe.volume.node.SceneNode
import ru.nsu.fit.g20204.kuznetsov.wireframe.volume.scene_viewer.ViewerFrame
import javax.swing.WindowConstants

object Main {
    lateinit var modelNode: ModelNode
    lateinit var camera: CameraNode
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