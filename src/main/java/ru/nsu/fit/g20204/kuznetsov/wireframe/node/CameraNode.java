package ru.nsu.fit.g20204.kuznetsov.wireframe.node;

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Matrix;

public class CameraNode extends Node {
    private double cameraViewPortWidth = 4;
    private double cameraViewPortHeight = 2;

    private double nearClippingPlane = 3;
    private double farClippingPlane = 40;

    public CameraNode(Node parentNode) {
        super(parentNode);
    }

    public Matrix getViewportTransform() {
        return new Matrix(new double[][] {
                {nearClippingPlane, 0, 0, 0},
                {0, nearClippingPlane, 0, 0},
                {0, 0, farClippingPlane / (farClippingPlane - nearClippingPlane), farClippingPlane * nearClippingPlane / (farClippingPlane - nearClippingPlane)},
                {0, 0, 1.0, 0.0}
        });
    }

    public double getViewPortHeight() {
        return cameraViewPortHeight;
    }
    public double getViewPortWidth() {
        return cameraViewPortWidth;
    }
    public void setViewPortWidth(double width) {
        this.cameraViewPortWidth = width;
    }
    public void setViewPortHeight(double height) {
        this.cameraViewPortHeight = height;
    }

    public double getNearClippingPlane() {
        return nearClippingPlane;
    }
    public void setNearClippingPlane(double nearClippingPlane) {
        this.nearClippingPlane = nearClippingPlane;
    }
    public double getFarClippingPlane() {
        return farClippingPlane;
    }
    public void setFarClippingPlane(double farClippingPlane) {
        this.farClippingPlane = farClippingPlane;
    }
}
