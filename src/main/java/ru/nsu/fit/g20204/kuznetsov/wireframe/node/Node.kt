package ru.nsu.fit.g20204.kuznetsov.wireframe.node

import java.util.LinkedList

open class Node(val parent: Node?) {
    private val childNodes: MutableList<Node> = LinkedList()
    private var localTranformMatrix: Matrix = Matrix()
    fun addChild(node: Node) {
        childNodes.add(node)
    }

    fun getChildNodes(): List<Node> {
        return childNodes
    }

    fun getLocalTranformMatrix(): Matrix {
        return localTranformMatrix
    }

    val globalTransform: Matrix
        get() = if (parent == null) {
            localTranformMatrix
        } else parent.globalTransform.multiply(localTranformMatrix)

    fun translate(dx: Double, dy: Double, dz: Double) {
        localTranformMatrix = localTranformMatrix.translate(dx, dy, dz)
    }

    fun scale(xScale: Double, yScale: Double, zScale: Double) {
        localTranformMatrix = localTranformMatrix.scale(xScale, yScale, zScale)
    }

    fun rotate(axis: Vector?, angle: Double) {
        localTranformMatrix = localTranformMatrix.rotate(axis, angle)
    }

    fun setLocalTransformMatrix(matrix: Matrix) {
        localTranformMatrix = matrix
    }
}
