package ru.nsu.fit.g20204.kuznetsov.wireframe.node

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Matrix
import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Vector
import java.util.*

open class Node(val parent: Node?) {
    val childNodes: MutableList<Node> = LinkedList()
    var localTransformMatrix: Matrix = Matrix()

    fun addChild(node: Node) {
        childNodes.add(node)
    }

    val globalTransform: Matrix
        get() = parent?.globalTransform?.multiply(localTransformMatrix) ?: localTransformMatrix

    fun translate(dx: Double, dy: Double, dz: Double) {
        localTransformMatrix = localTransformMatrix.translate(dx, dy, dz)
    }

    fun scale(xScale: Double, yScale: Double, zScale: Double) {
        localTransformMatrix = localTransformMatrix.scale(xScale, yScale, zScale)
    }

    fun rotate(axis: Vector, angle: Double) {
        localTransformMatrix = localTransformMatrix.rotate(axis, angle)
    }
}
