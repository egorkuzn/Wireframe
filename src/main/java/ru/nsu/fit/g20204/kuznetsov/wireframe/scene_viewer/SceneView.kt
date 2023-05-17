package ru.nsu.fit.g20204.kuznetsov.wireframe.scene_viewer

class SceneView(private var scene: SceneNode, private var camera: CameraNode?) : JPanel() {
    var edgeColor = Color(0, 0, 0)
    private var model: ModelNode?
    private var rotateScreenOrigin: Point? = null

    init {
        model = scene.model
        val sceneView = this
        val mouseAdapter: MouseAdapter = object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                sceneView.mousePressed(e)
            }

            override fun mouseDragged(e: MouseEvent) {
                sceneView.mouseDragged(e)
            }

            override fun mouseWheelMoved(e: MouseWheelEvent) {
                sceneView.mouseWheelMoved(e)
            }
        }
        addMouseListener(mouseAdapter)
        addMouseMotionListener(mouseAdapter)
        addMouseWheelListener(mouseAdapter)
        repaint()
    }

    fun mousePressed(e: MouseEvent) {
        rotateScreenOrigin = e.point
    }

    fun mouseDragged(e: MouseEvent) {
        if (rotateScreenOrigin == null) {
            rotateScreenOrigin = e.point
            return
        }
        val screenAxis = Point(e.x - rotateScreenOrigin!!.x, e.y - rotateScreenOrigin!!.y)
        rotateScreenOrigin = e.point
        if (screenAxis.x == 0 && screenAxis.y == 0) {
            return
        }
        val focusNodeAxis = Vector(screenAxis.y.toDouble(), -screenAxis.x.toDouble(), 0.0, 1.0)
        model!!.rotate(focusNodeAxis, rotationSpeed)
        this.repaint()
    }

    fun mouseWheelMoved(e: MouseWheelEvent) {
        val offset = e.preciseWheelRotation * zoomSpeed
        if (camera!!.nearClippingPlane <= 0 && offset >= 0) return
        camera!!.nearClippingPlane = camera!!.nearClippingPlane - offset
        camera!!.translate(0.0, 0.0, offset)
        this.repaint()
    }

    private fun paintEdges(g2d: Graphics2D, viewPortVertices: List<Vector?>, edgeList: List<Int>) {
        for (i in 0 until edgeList.size / 2) {
            val viewPortPoint1 = viewPortVertices[edgeList[2 * i]]
            val viewPortPoint2 = viewPortVertices[edgeList[2 * i + 1]]
            if (viewPortPoint1!!.z <= 0 || viewPortPoint1.z > 1 || viewPortPoint2!!.z <= 0 || viewPortPoint2.z > 1) {
                continue
            }
            val screenPoint1 = getScreenPoint(viewPortPoint1)
            val screenPoint2 = getScreenPoint(viewPortPoint2)
            val color = getColorByDistance(edgeColor, Math.min(viewPortPoint1.z, viewPortPoint2!!.z))
            g2d.color = color
            g2d.stroke = BasicStroke(
                (5 * (1 - Math.min(viewPortPoint1.z, viewPortPoint2.z))).toInt().toFloat(),
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_BEVEL
            )
            g2d.drawLine(screenPoint1.x, screenPoint1.y, screenPoint2.x, screenPoint2.y)
        }
    }

    fun setScene(scene: SceneNode, camera: CameraNode?) {
        this.scene = scene
        this.camera = camera
        model = scene.model
    }

    private fun getScreenPoint(viewPortPoint: Vector?): Point {
        val x = (viewPortPoint!!.x * width / camera!!.viewPortWidth).toInt() + width / 2
        val y = (viewPortPoint.y * height / camera!!.viewPortHeight).toInt() + height / 2
        return Point(x, y)
    }

    private fun getColorByDistance(color: Color, distance: Double): Color {
        var distance = distance
        distance = 1 - distance
        val rgb = color.rgb
        val red = Math.min(Math.max(((rgb shr 16 and 0xFF) * distance).toInt(), 0), 255)
        val green = Math.min(Math.max(((rgb shr 8 and 0xFF) * distance).toInt(), 0), 255)
        val blue = Math.min(Math.max(((rgb and 0xFF) * distance).toInt(), 0), 255)
        val newRgb = 0xFF shl 24 or (red shl 16) or (green shl 8) or blue
        return Color(newRgb)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2d = g as Graphics2D
        if (width < height) {
            camera!!.viewPortWidth = 1.5
            camera!!.viewPortHeight = 1.5 * height / width
        } else {
            camera!!.viewPortHeight = 1.5
            camera!!.viewPortWidth = 1.5 * width / height
        }
        val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val imageGraphics = bufferedImage.graphics as Graphics2D
        imageGraphics.color = Color(169, 169, 169)
        imageGraphics.fillRect(0, 0, bufferedImage.width, bufferedImage.height)
        paintNode(bufferedImage, scene)
        g2d.drawImage(bufferedImage, (width - bufferedImage.width) / 2, (height - bufferedImage.height) / 2, this)
        g2d.color = Color(40, 44, 52)
        g2d.drawRect(
            (width - bufferedImage.width) / 2,
            (height - bufferedImage.height) / 2,
            bufferedImage.width - 1,
            bufferedImage.height - 1
        )
    }

    private fun paintNode(bufferedImage: BufferedImage, node: Node) {
        if (node is ModelNode) {
            val modelNodeGlobalTransform = node.getGlobalTransform()
            val modelScaleTransform: Matrix = node.boundBoxMatrix
            val cameraGlobalTransform = camera!!.globalTransform
            val cameraViewportTransform = camera!!.viewportTransform
            val projectionMatrix = cameraViewportTransform.multiply(
                cameraGlobalTransform.multiply(
                    modelScaleTransform.multiply(
                        modelNodeGlobalTransform
                    )
                )
            )
            val geometry: Geometry = node.model
            val g = bufferedImage.graphics as Graphics2D
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            val center = Vector(0.0, 0.0, 0.0, 1.0)
            val viewPortCenter = projectionMatrix!!.multiply(center, true)
            val screenPointCenter = getScreenPoint(viewPortCenter)
            val paintAxis = BiFunction<Vector, Color, Void?> { vector: Vector, color: Color? ->
                g.color = color
                val viewPortAxis = projectionMatrix.multiply(vector, true)
                val screenPointAxis = getScreenPoint(viewPortAxis)
                g.drawLine(
                    screenPointCenter.x,
                    screenPointCenter.y,
                    screenPointAxis.x,
                    screenPointAxis.y
                )
                null
            }
            paintAxis.apply(Vector(1.0, 0.0, 0.0, 1.0), Color.RED)
            paintAxis.apply(Vector(0.0, 1.0, 0.0, 1.0), Color.GREEN)
            paintAxis.apply(Vector(0.0, 0.0, 1.0, 1.0), Color.BLUE)
            val viewPortVertices: MutableList<Vector?> = ArrayList()
            for (vertex in geometry.vertexList) {
                viewPortVertices.add(projectionMatrix.multiply(vertex, true))
            }
            paintEdges(g, viewPortVertices, geometry.edgeList)
        }
        for (childNode in node.childNodes) {
            paintNode(bufferedImage, childNode)
        }
    }

    companion object {
        private const val rotationSpeed = 3.0
        private const val zoomSpeed = 0.1
    }
}
