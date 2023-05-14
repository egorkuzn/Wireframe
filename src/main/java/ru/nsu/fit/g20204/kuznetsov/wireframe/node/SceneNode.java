package ru.nsu.fit.g20204.kuznetsov.wireframe.node;

public class SceneNode extends Node {
    public SceneNode(Node parentNode) {
        super(parentNode);
    }

    public CameraNode getCameraNode() {
        return new CameraNode(this);
    }
}
