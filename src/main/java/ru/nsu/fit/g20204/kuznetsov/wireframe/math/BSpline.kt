package ru.nsu.fit.g20204.kuznetsov.wireframe.math

import java.awt.geom.Point2D

/**
 * В данном коде описана алгебра B-сплайна
 */
class BSpline {
    private val DEFAULT_SEGMENT_SIZE = 10

    /**
     * @splineMatrix - матрица B-сплайна
     */
    private val splineMatrix = Matrix(
        arrayOf(
            doubleArrayOf(-1.0, 3.0, -3.0, 1.0),
            doubleArrayOf(3.0, -6.0, 3.0, 0.0),
            doubleArrayOf(-3.0, 0.0, 3.0, 0.0),
            doubleArrayOf(1.0, 4.0, 1.0, 0.0)
        )
    ).multiply(1.0 / 6)

    val keyPointList = ArrayList<Point2D.Double>()

    val splinePointList = ArrayList<Point2D.Double>()
    var splinePointsPerSegment = DEFAULT_SEGMENT_SIZE
        set(splinePointsPerSegment) {
            field = splinePointsPerSegment
            evaluateSpline()
        }

    fun addKeyPoint(keyPoint: Point2D.Double) {
        keyPointList.add(keyPoint)
        evaluateSpline()
    }

    fun setKeyPoint(index: Int, x: Double, y: Double) {
        keyPointList[index] = Point2D.Double(x, y)
        evaluateSpline()
    }

    fun setKeyPointX(dragPointIndex: Int, x: Double) {
        keyPointList[dragPointIndex].x = x
        evaluateSpline()
    }

    fun setKeyPointY(dragPointIndex: Int, y: Double) {
        keyPointList[dragPointIndex].y = y
        evaluateSpline()
    }

    fun removeKeyPoint(index: Int) {
        keyPointList.removeAt(index)
        evaluateSpline()
    }

    /**
     * В теории i = 2 .. N - 2
     * Но т.к. счёт индексов осуществляется с 0
     * то i = 1
     */
    private fun evaluateSpline() {
        splinePointList.clear()

        if (keyPointList.size < 4) return

        for (i in 1 until keyPointList.size - 2) {
            // Get polynomial coefficients by multiplying the spline matrix by component vectors
            val xCoefficients = splineMatrix.multiply(getXComponents(i), false)
            val yCoefficients = splineMatrix.multiply(getYComponents(i), false)
            splinePointsPerSegment(xCoefficients, yCoefficients)
        }
    }

    /**
     * У нас в модели имеется `keyPointList`, которая уже хранит
     * все управляющие точки.
     * @param i проходимся по каждой управляющей точке и берём 3 следующие.
     * Таким образом это есть участок, в котором строится сплайн
     * @return тем самым получаем Pi .. Pi+3 вектор
     */
    private fun getXComponents(i: Int): Vector {
        return Vector(
            keyPointList[i - 1].x,
            keyPointList[i].x,
            keyPointList[i + 1].x,
            keyPointList[i + 2].x
        )
    }

    private fun getYComponents(i: Int): Vector {
        return Vector(
            keyPointList[i - 1].y,
            keyPointList[i].y,
            keyPointList[i + 1].y,
            keyPointList[i + 2].y
        )
    }

    private fun splinePointsPerSegment(xCoefficients: Vector, yCoefficients: Vector) {
        var t: Double
        for (j in 0 until splinePointsPerSegment) {
            t = j.toDouble() / splinePointsPerSegment

            // Coordinates of the new point
            val x = xCoefficients.x * t * t * t + xCoefficients.y * t * t + xCoefficients.z * t + xCoefficients.w
            val y = yCoefficients.x * t * t * t + yCoefficients.y * t * t + yCoefficients.z * t + yCoefficients.w
            splinePointList.add(Point2D.Double(x, y))
        }
    }
}
