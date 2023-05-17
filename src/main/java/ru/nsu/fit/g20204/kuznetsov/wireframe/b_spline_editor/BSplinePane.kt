package ru.nsu.fit.g20204.kuznetsov.wireframe.b_spline_editor

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.BSpline
import java.awt.*
import java.awt.event.*
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import javax.swing.JPanel

class BSplinePane : JPanel(), MouseWheelListener, MouseMotionListener, MouseListener {
    private val backgroundColor = Color(169, 169, 169)
    private val xAxisColor = Color(51, 51, 51)
    private val yAxisColor = Color(51, 51, 51)
    private val keyPointColor = Color(42, 122, 255)
    private val selectedKeyPointColor = Color(255, 0, 0)
    private val brokenLineColor = Color(42, 46, 54)
    private val splineColor = Color(94, 255, 0)
    private val axisSize = 2
    private val splineSize = 2
    private val brokenLineSize = 1
    private val pointRadius = 10
    private var pixelsPerIndentStep: Int = INDENT_STEP_ON_DEFAULT_ZOOM
    private var zoom = 100.0
    private var verticalOffset = 0
    private var horizontalOffset = 0
    private var dragOrigin: Point? = null
    private var dragPointIndex = 0
    var pointModifiedListeners: MutableList<Unit> = ArrayList()
    val spline = BSpline()
    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2d = g as Graphics2D
        g2d.color = backgroundColor
        g2d.fillRect(0, 0, width, height)
        paintAxes(g2d)
        paintSpline(g2d)
    }

    private var verticalPosition = 0
    private var horizontalPosition = 0

    init {
        addMouseWheelListener(this)
        addMouseListener(this)
        addMouseMotionListener(this)
    }

    private fun paintAxes(g2d: Graphics2D) {
        g2d.stroke = BasicStroke(axisSize.toFloat())
        verticalPosition = height / 2 + verticalOffset
        horizontalPosition = width / 2 + horizontalOffset
        paintXAxes(g2d)
        paintYAxes(g2d)
    }

    private fun paintXAxes(g2d: Graphics2D) {
        g2d.color = xAxisColor
        g2d.drawLine(0, verticalPosition, width - 1, verticalPosition)
        run {
            var i = horizontalPosition + pixelsPerIndentStep
            while (i <= width) {
                g2d.drawLine(i, verticalPosition - axisSize * 2, i, verticalPosition + axisSize * 2)
                i += pixelsPerIndentStep
            }
        }
        var i = horizontalPosition - pixelsPerIndentStep
        while (i > 0) {
            g2d.drawLine(i, verticalPosition - axisSize * 2, i, verticalPosition + axisSize * 2)
            i -= pixelsPerIndentStep
        }
    }

    private fun paintYAxes(g2d: Graphics2D) {
        g2d.color = yAxisColor
        g2d.drawLine(horizontalPosition, 0, horizontalPosition, height - 1)
        run {
            var i = verticalPosition + pixelsPerIndentStep
            while (i <= height) {
                g2d.drawLine(horizontalPosition + axisSize * 2, i, horizontalPosition - axisSize * 2, i)
                i += pixelsPerIndentStep
            }
        }
        var i = verticalPosition - pixelsPerIndentStep
        while (i > 0) {
            g2d.drawLine(horizontalPosition + axisSize * 2, i, horizontalPosition - axisSize * 2, i)
            i -= pixelsPerIndentStep
        }
    }

    private fun paintSpline(g2d: Graphics2D) {
        if (spline.keyPoints.isEmpty()) {
            return
        }
        connectPointsWithStraightLines(g2d)
        showKeyPoints(g2d)
        showSpline(g2d)
    }

    private fun connectPointsWithStraightLines(g2d: Graphics2D) {
        g2d.stroke = BasicStroke(brokenLineSize.toFloat())
        g2d.color = brokenLineColor
        var previousScreenKeyPoint = getPointOnScreen(spline.keyPoints[0])
        for (point in spline.keyPoints) {
            val screenPoint = getPointOnScreen(point)
            g2d.drawLine(previousScreenKeyPoint.x, previousScreenKeyPoint.y, screenPoint.x, screenPoint.y)
            previousScreenKeyPoint = screenPoint
        }
    }

    private fun getPointOnScreen(point: Point2D.Double?): Point {
        return getPointOnScreen(point!!.getX(), point.getY())
    }

    private fun getPointOnScreen(x: Double, y: Double): Point {
        val xPixelDistance = (x * pixelsPerIndentStep).toInt()
        val yPixelDistance = (-y * pixelsPerIndentStep).toInt()
        val xAbsolutePos = xPixelDistance + (width / 2 + horizontalOffset)
        val yAbsolutePos = yPixelDistance + (height / 2 + verticalOffset)
        return Point(xAbsolutePos, yAbsolutePos)
    }

    private fun getContinuousPoint(x: Int, y: Int): Point2D.Double {
        val xPixelDistance = x - (width / 2 + horizontalOffset)
        val yPixelDistance = y - (height / 2 + verticalOffset)
        val xContinuous = xPixelDistance.toDouble() / pixelsPerIndentStep
        val yContinuous = yPixelDistance.toDouble() / pixelsPerIndentStep
        return Point2D.Double(xContinuous, -yContinuous)
    }

    private fun showKeyPoints(g2d: Graphics2D) {
        g2d.color = keyPointColor
        for (keyPointIndex in spline.keyPoints.indices) {
            val keyPoint = spline.keyPoints[keyPointIndex]
            val screenPoint = this.getPointOnScreen(keyPoint!!.x, keyPoint.y)
            if (keyPointIndex == dragPointIndex) g2d.color = selectedKeyPointColor else g2d.color = keyPointColor
            g2d.drawOval(screenPoint.x - pointRadius, screenPoint.y - pointRadius, pointRadius * 2, pointRadius * 2)
        }
    }

    private fun showSpline(g2d: Graphics2D) {
        g2d.stroke = BasicStroke(splineSize.toFloat())
        g2d.color = splineColor
        val splinePoints = spline.splinePoints
        if (!splinePoints!!.isEmpty()) {
            var previousScreenSplinePoint = getPointOnScreen(splinePoints[0])
            for (point in splinePoints) {
                val screenPoint = getPointOnScreen(point)
                g2d.drawLine(previousScreenSplinePoint.x, previousScreenSplinePoint.y, screenPoint.x, screenPoint.y)
                previousScreenSplinePoint = screenPoint
            }
        }
    }

    fun setZoom(zoom: Double) {
        if (zoom <= 0 || zoom > 800) {
            return
        }
        this.zoom = zoom
        pixelsPerIndentStep = (INDENT_STEP_ON_DEFAULT_ZOOM / (zoom / 100)).toInt()
        this.repaint()
    }

    private fun findSelectedKeyPoint(point: Point): Int {
        for (pointIndex in spline.keyPoints.indices) {
            val keyPoint = spline.keyPoints[pointIndex]
            val pointOnScreen = this.getPointOnScreen(keyPoint!!.x, keyPoint.y)
            pointOnScreen.x -= pointRadius
            pointOnScreen.y -= pointRadius
            val newBounds: Rectangle2D = Rectangle(
                pointOnScreen,
                Dimension(pointRadius * 2, pointRadius * 2)
            )
            if (newBounds.contains(point)) {
                return pointIndex
            }
        }
        return -1
    }

    override fun mouseWheelMoved(e: MouseWheelEvent) {
        setZoom(zoom + 10 * e.preciseWheelRotation)
    }

    override fun mouseDragged(e: MouseEvent) {
        if (dragOrigin != null) {
            paneDrag(e)
        } else if (dragPointIndex != -1) {
            keyPointDrag(e)
        }
        repaint()
    }

    override fun mouseMoved(e: MouseEvent) {}
    private fun paneDrag(e: MouseEvent) {
        val deltaX = dragOrigin!!.x - e.x
        val deltaY = dragOrigin!!.y - e.y
        horizontalOffset -= deltaX
        verticalOffset -= deltaY
        dragOrigin = Point(e.point)
    }

    private fun keyPointDrag(e: MouseEvent) {
        val continuousPoint = getContinuousPoint(e.x, e.y)
        spline.setKeyPoint(
            dragPointIndex,
            continuousPoint.x,
            continuousPoint.y
        )
        for (l in pointModifiedListeners) {
            l.apply(dragPointIndex, spline.keyPoints[dragPointIndex])
        }
    }

    override fun mousePressed(e: MouseEvent) {
        val pointIndex = findSelectedKeyPoint(e.point)
        if (pointIndex != -1) {
            if (e.button == MouseEvent.BUTTON1) {
                dragPoint(pointIndex)
            } else if (e.button == MouseEvent.BUTTON3) {
                removeSplinePoint(pointIndex)
            }
            return
        }
        dragOrigin = Point(e.point)
    }

    private fun dragPoint(pointIndex: Int) {
        dragPointIndex = pointIndex
        for (l in pointModifiedListeners) {
            l.apply(dragPointIndex, spline.keyPoints[dragPointIndex])
        }
    }

    private fun removeSplinePoint(pointIndex: Int) {
        spline.removeKeyPoint(pointIndex)
        dragPointIndex = -1
        for (l in pointModifiedListeners) {
            l.apply(dragPointIndex, Point2D.Double(0.0, 0.0))
        }
    }

    override fun mouseClicked(e: MouseEvent) {
        val pointIndex = findSelectedKeyPoint(e.point)
        if (pointIndex != -1) {
            dragPointIndex = pointIndex
            return
        }
        if (e.button == MouseEvent.BUTTON1) {
            addKeyButton(e)
        }
        this.repaint()
    }

    private fun addKeyButton(e: MouseEvent) {
        val point = getContinuousPoint(e.x, e.y)
        spline.addKeyPoint(point)
        dragPointIndex = spline.keyPoints.size - 1
        for (l in pointModifiedListeners) {
            l.apply(dragPointIndex, spline.keyPointList[dragPointIndex])
        }
    }

    fun addPointModifiedListener(listener: Unit) {
        pointModifiedListeners.add(listener)
    }

    fun setSelectedX(x: Double) {
        spline.setKeyPointX(dragPointIndex, x)
        this.repaint()
    }

    fun setSelectedY(y: Double) {
        spline.setKeyPointY(dragPointIndex, y)
        this.repaint()
    }

    fun setSplinePointsPerSegment(splinePointsPerSegment: Int) {
        spline.splinePointsPerSegment = splinePointsPerSegment
        this.repaint()
    }

    override fun mouseReleased(e: MouseEvent) {
        dragOrigin = null
    }

    override fun mouseExited(e: MouseEvent) {
        dragOrigin = null
    }

    override fun mouseEntered(e: MouseEvent) {}

    companion object {
        private const val INDENT_STEP_ON_DEFAULT_ZOOM = 64
    }
}
