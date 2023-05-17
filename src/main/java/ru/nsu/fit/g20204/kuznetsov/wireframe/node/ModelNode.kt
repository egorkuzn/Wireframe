package ru.nsu.fit.g20204.kuznetsov.wireframe.node

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Matrix
import ru.nsu.fit.g20204.kuznetsov.wireframe.model.Geometry

class ModelNode(parentNode: Node?) : Node(parentNode) {
    private var model: Geometry? = null
    fun setModel(model: Geometry?) {
        if (model != null) {
            this.model = model
        }
    }

    fun getModel(): Geometry? {
        return model
    }

    val boundBoxMatrix: Matrix
        // Получаем матрицу, чтобы моделька вмещалась в бокс
        get() {
            var xMax = model!!.vertexList[0].x
            var yMax = model!!.vertexList[0].y
            var zMax = model!!.vertexList[0].z
            var xMin = model!!.vertexList[0].x
            var yMin = model!!.vertexList[0].y
            var zMin = model!!.vertexList[0].z
            for (vertex in model!!.vertexList) {
                xMax = Math.max(vertex.x, xMax)
                xMin = Math.min(vertex.x, xMin)
                yMax = Math.max(vertex.y, yMax)
                yMin = Math.min(vertex.y, yMin)
                zMax = Math.max(vertex.z, zMax)
                zMin = Math.min(vertex.z, zMin)
            }
            val scale = Math.max(Math.max(xMax - xMin, yMax - yMin), zMax - zMin)
            return Matrix.Companion.getScaleMatrix(1 / scale, 1 / scale, 1 / scale)
        }
}
