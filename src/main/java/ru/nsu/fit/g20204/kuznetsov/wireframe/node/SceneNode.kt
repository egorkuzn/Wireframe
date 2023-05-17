package ru.nsu.fit.g20204.kuznetsov.wireframe.node;

import java.util.ArrayList;
import java.util.List;

public class SceneNode extends Node {
    private List<CameraNode> cameraNodeList = new ArrayList<>();

    private ModelNode modelNode;

    public ModelNode getModel() {
        return modelNode;
    }

    public SceneNode(Node parentNode) {
        super(parentNode);
    }

    public CameraNode createCameraNode() {
        CameraNode camera = new CameraNode(this);
        this.addChild(camera);
        cameraNodeList.add(camera);

        return camera;
    }

    public void addCameraNode(CameraNode cameraNode) {
        cameraNodeList.add(cameraNode);
    }

    public List<CameraNode> getCameraList() {
        return cameraNodeList;
    }

    public void setModel(ModelNode modelNode) {
        this.modelNode = modelNode;
    }
}
