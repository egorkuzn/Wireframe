package ru.nsu.fit.g20204.kuznetsov.wireframe.node;

public class CameraNode extends Node {
    private double cameraViewPortWidth = 4;
    private double cameraViewPortHeight = 2;

    private double nearClippingPlane = 3;
    private double farClippingPlane = 40;

    public CameraNode(Node parentNode) {
        super(parentNode);
    }


}
