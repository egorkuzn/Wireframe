package ru.nsu.fit.g20204.kuznetsov.wireframe.volume.node

import ru.nsu.fit.g20204.kuznetsov.wireframe.util.math.Matrix
import ru.nsu.fit.g20204.kuznetsov.wireframe.util.model.Geometry
import kotlin.math.max
import kotlin.math.min

class ModelNode(parentNode: Node?) : Node(parentNode) {
    lateinit var model: Geometry

    val boundBoxMatrix: Matrix
        // Получаем матрицу, чтобы моделька вмещалась в бокс
        get() {
            var xMax = model.vertexList[0].x
            var yMax = model.vertexList[0].y
            var zMax = model.vertexList[0].z
            var xMin = model.vertexList[0].x
            var yMin = model.vertexList[0].y
            var zMin = model.vertexList[0].z

            for (vertex in model.vertexList) {
                xMax = max(vertex.x, xMax)
                xMin = min(vertex.x, xMin)
                yMax = max(vertex.y, yMax)
                yMin = min(vertex.y, yMin)
                zMax = max(vertex.z, zMax)
                zMin = min(vertex.z, zMin)
            }

            val scale = max(max(xMax - xMin, yMax - yMin), zMax - zMin)
            return Matrix.Companion.getScaleMatrix(1 / scale, 1 / scale, 1 / scale)
        }
}
