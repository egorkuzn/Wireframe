package ru.nsu.fit.g20204.kuznetsov.wireframe.util

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Matrix
import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Vector
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.ModelNode
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.SceneNode
import java.io.*

interface ModelParser {
    companion object {
        fun fileToScene(file: File?): SceneNode? {
            try {
                DataInputStream(FileInputStream(file)).use { input ->
                    val scene = SceneNode(null)
                    scene.setLocalTransformMatrix(streamToMatrix(input))
                    val camera = scene.createCameraNode()
                    camera!!.setLocalTransformMatrix(streamToMatrix(input))
                    camera.nearClippingPlane = input.readDouble()
                    camera.farClippingPlane = input.readDouble()
                    camera.viewPortWidth = input.readDouble()
                    camera.viewPortHeight = input.readDouble()
                    val modelNode = ModelNode(scene)
                    modelNode.setLocalTransformMatrix(streamToMatrix(input))
                    modelNode.model = streamToGeometry(input)
                    scene.addChild(modelNode)
                    scene.model = modelNode
                    return scene
                }
            } catch (e: IOException) {
                return null
            }
        }

        fun sceneToFile(scene: SceneNode, file: File?): Boolean {
            try {
                DataOutputStream(FileOutputStream(file)).use { output ->
                    matrixToStream(scene.localTranformMatrix, output)
                    val camera = scene.cameraList[0]
                    matrixToStream(camera!!.localTranformMatrix, output)
                    output.writeDouble(camera.nearClippingPlane)
                    output.writeDouble(camera.farClippingPlane)
                    output.writeDouble(camera.viewPortWidth)
                    output.writeDouble(camera.viewPortHeight)
                    val modelNode = scene.model
                    matrixToStream(modelNode!!.localTranformMatrix, output)
                    geometryToStream(modelNode.model, output)
                }
            } catch (e: IOException) {
                return false
            }
            return true
        }

        @Throws(IOException::class)
        fun streamToGeometry(input: DataInputStream): Geometry? {
            val vertexList: MutableList<Vector> = ArrayList()
            val edgeList: MutableList<Int> = ArrayList()
            val vertexCount = input.readInt()
            for (vertexIndex in 0 until vertexCount) {
                val vertex = Vector()
                vertex.x = input.readDouble()
                vertex.y = input.readDouble()
                vertex.z = input.readDouble()
                vertex.w = input.readDouble()
                vertexList.add(vertex)
            }
            val edgeVertexCount = input.readInt()
            for (vertexIndex in 0 until edgeVertexCount) {
                edgeList.add(input.readInt())
            }
            return Geometry(vertexList, edgeList)
        }

        @Throws(IOException::class)
        fun geometryToStream(geometry: Geometry?, output: DataOutputStream) {
            val vertexList = geometry!!.vertexList
            val edgeList = geometry.edgeList
            output.writeInt(vertexList.size)
            for (vertex in vertexList) {
                output.writeDouble(vertex.x)
                output.writeDouble(vertex.y)
                output.writeDouble(vertex.z)
                output.writeDouble(vertex.w)
            }
            output.writeInt(edgeList.size)
            for (vertexIndex in edgeList) {
                output.writeInt(vertexIndex!!)
            }
        }

        @Throws(IOException::class)
        fun matrixToStream(matrix: Matrix, output: DataOutputStream) {
            val matrixArray = matrix.matrix
            for (doubles in matrixArray!!) {
                for (j in matrixArray[0]!!.indices) {
                    output.writeDouble(doubles!![j])
                }
            }
        }

        @Throws(IOException::class)
        fun streamToMatrix(input: DataInputStream): Matrix? {
            val matrixArray = Array(4) { DoubleArray(4) }
            for (i in 0..3) {
                for (j in 0..3) {
                    matrixArray[i][j] = input.readDouble()
                }
            }
            return Matrix(matrixArray)
        }
    }
}
