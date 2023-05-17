package ru.nsu.fit.g20204.kuznetsov.wireframe.volume.scene_viewer

import ru.nsu.fit.g20204.kuznetsov.wireframe.util.math.Matrix
import ru.nsu.fit.g20204.kuznetsov.wireframe.util.math.Vector
import ru.nsu.fit.g20204.kuznetsov.wireframe.util.model.Geometry
import ru.nsu.fit.g20204.kuznetsov.wireframe.volume.node.CameraNode
import ru.nsu.fit.g20204.kuznetsov.wireframe.volume.node.ModelNode
import ru.nsu.fit.g20204.kuznetsov.wireframe.volume.node.Node
import ru.nsu.fit.g20204.kuznetsov.wireframe.volume.node.SceneNode
import ru.nsu.fit.g20204.kuznetsov.wireframe.util.ColorKeeper.backgroundColor
import ru.nsu.fit.g20204.kuznetsov.wireframe.util.ColorKeeper.frameColor
import ru.nsu.fit.g20204.kuznetsov.wireframe.util.ColorKeeper.splineColor
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent
import java.awt.image.BufferedImage
import javax.swing.JPanel
import kotlin.math.min

class SceneView(private var scene: SceneNode, private var camera: CameraNode) : JPanel() {
    private var model: ModelNode
    private lateinit var rotateScreenOrigin: Point

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
        val screenAxis = Point(e.x - rotateScreenOrigin.x, e.y - rotateScreenOrigin.y)

        rotateScreenOrigin = e.point

        if (screenAxis.x == 0 && screenAxis.y == 0) {
            return
        }
        val focusNodeAxis = Vector(screenAxis.y.toDouble(), -screenAxis.x.toDouble(), 0.0, 1.0)
        model.rotate(focusNodeAxis, rotationSpeed)
        this.repaint()
    }

    fun mouseWheelMoved(e: MouseWheelEvent) {
        val offset = e.preciseWheelRotation * zoomSpeed
        if (camera.nearClippingPlane <= 0 && offset >= 0) return
        camera.nearClippingPlane = camera.nearClippingPlane - offset
        camera.translate(0.0, 0.0, offset)
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
            g2d.color = splineColor
            g2d.drawLine(screenPoint1.x, screenPoint1.y, screenPoint2.x, screenPoint2.y)
        }
    }

    fun setScene(scene: SceneNode, camera: CameraNode) {
        this.scene = scene
        this.camera = camera
        model = scene.model
    }

    private fun getScreenPoint(viewPortPoint: Vector): Point {
        val x = (viewPortPoint.x * width / camera.viewPortWidth).toInt() + width / 2
        val y = (viewPortPoint.y * height / camera.viewPortHeight).toInt() + height / 2
        return Point(x, y)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2d = g as Graphics2D

        if (width < height) {
            camera.viewPortWidth = 1.5
            camera.viewPortHeight = 1.5 * height / width
        } else {
            camera.viewPortHeight = 1.5
            camera.viewPortWidth = 1.5 * width / height
        }

        val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val imageGraphics = bufferedImage.graphics as Graphics2D

        imageGraphics.color = backgroundColor
        imageGraphics.fillRect(0, 0, bufferedImage.width, bufferedImage.height)

        paintNode(bufferedImage, scene)

        g2d.drawImage(bufferedImage, (width - bufferedImage.width) / 2, (height - bufferedImage.height) / 2, this)

        g2d.color = frameColor
        g2d.drawRect(
            (width - bufferedImage.width) / 2,
            (height - bufferedImage.height) / 2,
            bufferedImage.width - 1,
            bufferedImage.height - 1
        )
    }

    private fun paintNode(bufferedImage: BufferedImage, node: Node) {
        if (node is ModelNode) {
            val projectionMatrix = camera.viewportTransform.multiply(
                camera.globalTransform.multiply(
                    node.boundBoxMatrix.multiply(
                        node.globalTransform
                    )
                )
            )

            val geometry: Geometry = node.model
            val g = bufferedImage.graphics as Graphics2D
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            val center = Vector(0.0, 0.0, 0.0, 1.0)
            val viewPortCenter = projectionMatrix.multiply(center, true)
            val screenPointCenter = getScreenPoint(viewPortCenter)

            val paintAxis = { vector: Vector, color: Color? ->
                g.color = color
                val viewPortAxis = projectionMatrix.multiply(vector, true)
                val screenPointAxis = getScreenPoint(viewPortAxis)
                g.drawLine(
                    screenPointCenter.x, screenPointCenter.y, screenPointAxis.x, screenPointAxis.y
                )
            }

            paintAxis(Vector(1.0, 0.0, 0.0, 1.0), Color.RED)
            paintAxis(Vector(0.0, 1.0, 0.0, 1.0), Color.GREEN)
            paintAxis(Vector(0.0, 0.0, 1.0, 1.0), Color.BLUE)

            val viewPortVertices = ArrayList<Vector>()

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
