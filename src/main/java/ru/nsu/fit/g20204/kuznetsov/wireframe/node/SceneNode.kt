package ru.nsu.fit.g20204.kuznetsov.wireframe.node

class SceneNode(parentNode: Node?) : Node(parentNode) {
    val cameraNodeList: MutableList<CameraNode> = ArrayList()
    lateinit var model: ModelNode
    fun createCameraNode(): CameraNode {
        val camera = CameraNode(this)
        addChild(camera)
        cameraNodeList.add(camera)
        return camera
    }

    fun addCameraNode(cameraNode: CameraNode) {
        cameraNodeList.add(cameraNode)
    }

    val cameraList: List<CameraNode>
        get() = cameraNodeList
}
