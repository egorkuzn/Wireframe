package ru.nsu.fit.g20204.kuznetsov.wireframe.volume.scene_viewer

import ru.nsu.fit.g20204.kuznetsov.wireframe.bspline.viewer.BSplineEditor
import ru.nsu.fit.g20204.kuznetsov.wireframe.util.math.Matrix
import ru.nsu.fit.g20204.kuznetsov.wireframe.util.model.Geometry
import ru.nsu.fit.g20204.kuznetsov.wireframe.volume.node.CameraNode
import ru.nsu.fit.g20204.kuznetsov.wireframe.volume.node.SceneNode
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame

class ViewerFrame(private var scene: SceneNode) : JFrame("3D Scene Viewer") {
    private val actions: HashMap<String, ActionListener>
    private val fileChooser = FileChooser(this)
    private val sceneView: SceneView

    init {
        minimumSize = Dimension(640, 480)
        setLocation(400, 160)
        isVisible = true
        actions = object : HashMap<String, ActionListener>() {
            init {
                put("Normalize view", ActionListener { _: ActionEvent ->
                    scene.model.localTransformMatrix = Matrix()
                    repaint()
                })
                put("BSpline editor", ActionListener { _: ActionEvent ->
                    val splineEditor = BSplineEditor()
                    splineEditor.addWindowListener(object : WindowAdapter() {
                        override fun windowClosing(e: WindowEvent) {
                            scene.model.model = splineEditor.splineModel
                            repaint()
                        }
                    })
                    splineEditor.addSplineModelChangeListener { m: Geometry ->
                        scene.model.model = m
                        repaint()
                    }
                })
                put("Save", ActionListener { _: ActionEvent -> fileChooser.showSaveDialog(scene) })
                put("Open", ActionListener { _: ActionEvent ->
                    val scene = fileChooser.showOpenDialog()
                    if (scene != null) setScene(scene, scene.cameraList[0])
                })
            }
        }
        val camera = scene.cameraList[0]
        sceneView = SceneView(scene, camera)
        add(sceneView)
        val toolBar = ToolBar(actions)
        add(toolBar, BorderLayout.PAGE_START)
        val menuBar = MenuBar(actions)
        jMenuBar = menuBar
        setScene(scene, camera)
        pack()
    }

    fun setScene(scene: SceneNode, camera: CameraNode) {
        this.scene = scene
        sceneView.setScene(scene, camera)
        this.repaint()
    }
}
