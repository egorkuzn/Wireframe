package ru.nsu.fit.g20204.kuznetsov.wireframe.model

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.BSpline
import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Matrix
import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Vector

interface ModelFactory {
    companion object {
        @Throws(IllegalArgumentException::class)
        fun createRoutedSplineModel(
            spline: BSpline,
            rotationCount: Int,
            alongLayerCount: Int,
            acrossLayerCount: Int
        ): Geometry {
            if (spline.splinePointList.isEmpty()) {
                return Geometry()
            }
            require(rotationCount >= alongLayerCount) { "alongLayerCount can't be greater than rotationCount" }
            require(spline.splinePointList.size >= acrossLayerCount) { "acrossLayerCount " + acrossLayerCount + " is greater than spline's point count " + spline.splinePointList.size }
            require(alongLayerCount > 0) { "alongLayerCount $alongLayerCount can't be zero" }
            val vertexList = ArrayList<Vector>()

            // Повернём каждую точку сплайна => получаем повенутый на угол сплайн
            // Сделаем поворот на одинаковый угол - получим вращение
            for (point in spline.splinePointList) {
                for (i in 0 until rotationCount) {
                    val rotationMatrix: Matrix = Matrix.Companion.getRotationMatrix(
                        Vector(1.0, 0.0, 0.0, 1.0),
                        360 * i.toDouble() / rotationCount
                    )
                    vertexList.add(rotationMatrix.multiply(Vector(point.x, point.y, 0.0, 1.0), true))
                }
            }

            // У нас есть сплайн с готовыми точками. Из них нужно выбрать те, которые мы будем считать
            // пересечениями с плоскостями
            val edgeList = ArrayList(getSplineAcrossEdges(rotationCount, acrossLayerCount, spline.splinePointList.size))

            // Как получали вершины повернутых сплайнов, так и соединяем нужные
            // Сначала идем по сплайну (возможно пропуская некоторые точки)
            // Т.е. получается некоторая ось. После получения можем переходить к отрисовке следующей
            for (splineIndex in 0 until spline.splinePointList.size - 1) {
                for (layer in 0 until alongLayerCount) {
                    edgeList.add(splineIndex * rotationCount + layer * rotationCount / alongLayerCount)
                    edgeList.add((splineIndex + 1) * rotationCount + layer * rotationCount / alongLayerCount)
                }
            }
            return Geometry(vertexList, edgeList)
        }

        private fun getSplineAcrossEdges(rotationCount: Int, acrossLayerCount: Int, splineSize: Int): List<Int> {
            val edgeList = ArrayList<Int>()
            // Т.к. полчаемый массив точек фигуры вращения получется сначала из вращения одной точки сплайна,
            // то пересекающий сплайн первй слой будет [0; rotationCount].
            if (acrossLayerCount >= 1) {
                for (vertexIndex in 0 until rotationCount) {
                    edgeList.add(vertexIndex)
                    if ((vertexIndex + 1) % rotationCount == 0) {
                        edgeList.add(vertexIndex - rotationCount + 1)
                    } else {
                        edgeList.add(vertexIndex + 1)
                    }
                }
            }

            // Для случая нескольких слоёв мы просто будем "поднимать" первый.
            if (acrossLayerCount >= 2) {
                val betweenStepSpace = (splineSize - acrossLayerCount) / (acrossLayerCount - 1)
                val extraBetweenSpace = (splineSize - acrossLayerCount) % (acrossLayerCount - 1)
                var extraCount = 0
                for (layer in 1 until acrossLayerCount) {
                    if (extraCount < extraBetweenSpace) {
                        extraCount++
                    }
                    for (vertexIndex in (betweenStepSpace * layer + extraCount + layer) * rotationCount until betweenStepSpace * layer * rotationCount + rotationCount) {
                        edgeList.add(vertexIndex)
                        if ((vertexIndex + 1) % rotationCount == 0) edgeList.add(vertexIndex - rotationCount + 1) else edgeList.add(
                            vertexIndex + 1
                        )
                    }
                }
            }
            return edgeList
        }
    }
}
