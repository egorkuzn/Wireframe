package ru.nsu.fit.g20204.kuznetsov.wireframe.scene_viewer

class ViewerFrame(private var scene: SceneNode) : JFrame("3D Scene Viewer") {
    private val actions: HashMap<String, ActionListener>
    private val fileChooser = FileChooser(this)
    private val sceneView: SceneView

    init {
        SwingUtilities.updateComponentTreeUI(this)
        minimumSize = Dimension(640, 480)
        setLocation(400, 160)
        isVisible = true
        actions = object : HashMap<String?, ActionListener?>() {
            init {
                put("Normalize view", ActionListener { e: ActionEvent? ->
                    scene.model.setLocalTransformMatrix(Matrix())
                    repaint()
                })
                put("BSpline editor", ActionListener { e: ActionEvent? ->
                    val splineEditor = BSplineEditor()
                    splineEditor.addWindowListener(object : WindowAdapter() {
                        override fun windowClosing(e: WindowEvent) {
                            scene.model.model = splineEditor.splineModel
                            repaint()
                        }
                    })
                    splineEditor.addSplineModelChangeListener { m: Geometry? ->
                        scene.model.model = m
                        repaint()
                        null
                    }
                })
                put("Save", ActionListener { e: ActionEvent? -> fileChooser.showSaveDialog(scene) })
                put("Open", ActionListener { e: ActionEvent? ->
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

    fun setScene(scene: SceneNode, camera: CameraNode?) {
        this.scene = scene
        sceneView.setScene(scene, camera)
        this.repaint()
    }
}
