package ru.nsu.fit.g20204.kuznetsov.wireframe.node

class CameraNode(parentNode: Node?) : Node(parentNode) {
    var viewPortWidth = 4.0
    var viewPortHeight = 2.0
    var nearClippingPlane = 3.0
    var farClippingPlane = 40.0
    val viewportTransform: Matrix
        get() = Matrix(
            arrayOf<DoubleArray>(
                doubleArrayOf(nearClippingPlane, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, nearClippingPlane, 0.0, 0.0),
                doubleArrayOf(
                    0.0,
                    0.0,
                    farClippingPlane / (farClippingPlane - nearClippingPlane),
                    farClippingPlane * nearClippingPlane / (farClippingPlane - nearClippingPlane)
                ),
                doubleArrayOf(0.0, 0.0, 1.0, 0.0)
            )
        )
}
