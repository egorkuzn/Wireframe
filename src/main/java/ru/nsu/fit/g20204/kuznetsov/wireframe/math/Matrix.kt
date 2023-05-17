package ru.nsu.fit.g20204.kuznetsov.wireframe.math

import java.util.*

class Matrix {
    var matrix = Array(SIZE) { DoubleArray(SIZE) }

    constructor() {
        matrix = arrayOf(
            doubleArrayOf(1.0, 0.0, 0.0, 0.0),
            doubleArrayOf(0.0, 1.0, 0.0, 0.0),
            doubleArrayOf(0.0, 0.0, 1.0, 0.0),
            doubleArrayOf(0.0, 0.0, 0.0, 1.0)
        )
    }

    constructor(matrix: Array<DoubleArray>) {
        if (matrix.size != SIZE || matrix[0].size != SIZE) return
        for (i in 0..3) {
            this.matrix[i] = Arrays.copyOf(matrix[i], SIZE)
        }
    }

    fun multiply(value: Double): Matrix {
        val newMatrix = Array(SIZE) { DoubleArray(SIZE) }
        for (i in 0 until SIZE) {
            for (j in 0..3) {
                newMatrix[i][j] = matrix[i][j] * value
            }
        }
        return Matrix(newMatrix)
    }

    fun multiply(vector: Vector, wCorrect: Boolean): Vector {
        val resultVector = Vector()
        resultVector.x =
            vector.x * matrix[0][0] + vector.y * matrix[0][1] + vector.z * matrix[0][2] + vector.w * matrix[0][3]
        resultVector.y =
            vector.x * matrix[1][0] + vector.y * matrix[1][1] + vector.z * matrix[1][2] + vector.w * matrix[1][3]
        resultVector.z =
            vector.x * matrix[2][0] + vector.y * matrix[2][1] + vector.z * matrix[2][2] + vector.w * matrix[2][3]
        resultVector.w =
            vector.x * matrix[3][0] + vector.y * matrix[3][1] + vector.z * matrix[3][2] + vector.w * matrix[3][3]
        if (wCorrect) {
            resultVector.correctW()
        }
        return resultVector
    }

    fun multiply(other: Matrix?): Matrix {
        val newMatrixArray = Array(4) { DoubleArray(4) }
        for (i in 0..3) {
            Arrays.fill(newMatrixArray[i], 0.0)
        }
        for (i in 0..3) {
            for (j in 0..3) {
                for (k in 0..3) {
                    newMatrixArray[i][k] += matrix[i][j] * other!!.matrix[j][k]
                }
            }
        }
        return Matrix(newMatrixArray)
    }

    fun translate(dx: Double, dy: Double, dz: Double): Matrix {
        val translationMatrix = getTranslationMatrix(dx, dy, dz)
        return translationMatrix.multiply(this)
    }

    fun rotate(axis: Vector, angle: Double): Matrix {
        val rotationMatrix = getRotationMatrix(axis, angle)
        return rotationMatrix.multiply(this)
    }

    fun scale(xScale: Double, yScale: Double, zScale: Double): Matrix {
        val scaleMatrix = getScaleMatrix(xScale, yScale, zScale)
        return scaleMatrix.multiply(this)
    }

    override fun toString(): String {
        return Arrays.deepToString(matrix)
    }

    companion object {
        private const val SIZE = 4
        private fun getTranslationMatrix(dx: Double, dy: Double, dz: Double): Matrix {
            return Matrix(
                arrayOf(
                    doubleArrayOf(1.0, 0.0, 0.0, dx),
                    doubleArrayOf(0.0, 1.0, 0.0, dy),
                    doubleArrayOf(0.0, 0.0, 1.0, dz),
                    doubleArrayOf(0.0, 0.0, 0.0, 1.0)
                )
            )
        }

        fun getScaleMatrix(xScale: Double, yScale: Double, zScale: Double): Matrix {
            return Matrix(
                arrayOf(
                    doubleArrayOf(xScale, 0.0, 0.0, 0.0),
                    doubleArrayOf(0.0, yScale, 0.0, 0.0),
                    doubleArrayOf(0.0, 0.0, zScale, 0.0),
                    doubleArrayOf(0.0, 0.0, 0.0, 1.0)
                )
            )
        }

        fun getRotationMatrix(axis: Vector, angleDegrees: Double): Matrix {
            axis.normalize()
            val x = axis.x
            val y = axis.y
            val z = axis.z
            val angle = angleDegrees * 2 * Math.PI / 360
            return Matrix(
                arrayOf(
                    doubleArrayOf(
                        Math.cos(angle) + (1 - Math.cos(angle)) * x * x,
                        (1 - Math.cos(angle)) * x * y - Math.sin(angle) * z,
                        (1 - Math.cos(angle)) * x * z + Math.sin(angle) * y,
                        0.0
                    ),
                    doubleArrayOf(
                        (1 - Math.cos(angle)) * x * y + Math.sin(angle) * z,
                        Math.cos(angle) + (1 - Math.cos(angle)) * y * y,
                        (1 - Math.cos(angle)) * y * z - Math.sin(angle) * x,
                        0.0
                    ),
                    doubleArrayOf(
                        (1 - Math.cos(angle)) * x * z - Math.sin(angle) * y,
                        (1 - Math.cos(angle)) * y * z + Math.sin(angle) * x,
                        Math.cos(angle) + (1 - Math.cos(angle)) * z * z,
                        0.0
                    ),
                    doubleArrayOf(0.0, 0.0, 0.0, 1.0)
                )
            )
        }
    }
}
