package ru.nsu.fit.g20204.kuznetsov.wireframe.model

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Vector

class Geometry {
    private val vectorList: MutableList<Vector>
    val edgeList: List<Int>

    constructor() {
        vectorList = ArrayList()
        edgeList = ArrayList()
        vectorList.add(Vector(1.0, 1.0, 1.0, 1.0))
        vectorList.add(Vector(0.0, 0.0, 0.0, 1.0))
    }

    constructor(vertexList: MutableList<Vector>, edgeList: List<Int>) {
        vectorList = vertexList
        this.edgeList = edgeList
    }

    val vertexList: List<Vector>
        get() = vectorList
}
