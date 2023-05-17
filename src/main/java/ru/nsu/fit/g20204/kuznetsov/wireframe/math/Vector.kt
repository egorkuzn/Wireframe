package ru.nsu.fit.g20204.kuznetsov.wireframe.math

class Vector {
    var x = 0.0
    var y = 0.0
    var z = 0.0
    var w = 0.0

    constructor(x: Double, y: Double, z: Double, w: Double) {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
    }

    constructor(arrayVector: DoubleArray) {
        if (arrayVector.size < 4) return
        x = arrayVector[0]
        y = arrayVector[1]
        z = arrayVector[2]
        w = arrayVector[3]
    }

    constructor()

    fun multiply(m: Matrix, wCorrect: Boolean): Vector {
        val resultVector = Vector()
        val matrix = m.matrix
        resultVector.x = x * matrix[0][0] + y * matrix[1][0] + z * matrix[2][0] + w * matrix[3][0]
        resultVector.y = x * matrix[0][1] + y * matrix[1][1] + z * matrix[2][1] + w * matrix[3][1]
        resultVector.z = x * matrix[0][2] + y * matrix[1][2] + z * matrix[2][2] + w * matrix[3][2]
        resultVector.w = x * matrix[0][3] + y * matrix[1][3] + z * matrix[2][3] + w * matrix[3][3]
        if (wCorrect) {
            resultVector.correctW()
        }
        return resultVector
    }

    /**
     * Changes vector's length to 1 by dividing each component by current length.
     */
    fun normalize() {
        val divider = Math.sqrt(x * x + y * y + z * z)
        x /= divider
        y /= divider
        z /= divider
    }

    /**
     * Divides each component by w. In 3D graphics the vector represents the vertex correctly, if the 4th component w is equal to 1.
     */
    fun correctW() {
        if (w != 1.0) {
            x /= w
            y /= w
            z /= w
            w = 1.0
        }
    }

    val asArray: DoubleArray
        /**
         * Convert vector's coordinates to array.
         * @return double[] {x, y, z, w}
         */
        get() = doubleArrayOf(x, y, w, z)

    override fun toString(): String {
        return "($x, $y, $z, $w)"
    }
}
