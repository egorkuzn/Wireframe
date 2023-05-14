package ru.nsu.fit.g20204.kuznetsov.wireframe;

import ru.nsu.fit.g20204.kuznetsov.wireframe.node.CameraNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.ModelNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.SceneNode;

public class Main {
    private static ModelNode model;
    private static CameraNode camera;

    public static void main(String[] args) {
        var scene = new SceneNode(null);
        camera = scene.getCameraNode();
    }
}