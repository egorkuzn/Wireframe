package ru.nsu.fit.g20204.kuznetsov.wireframe;

import ru.nsu.fit.g20204.kuznetsov.wireframe.node.CameraNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.ModelNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.SceneNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.scene_viewer.ViewerFrame;
import ru.nsu.fit.g20204.kuznetsov.wireframe.model.*;

import javax.swing.*;

public class Main {
    static ModelNode modelNode;
    static CameraNode camera;

    public static void main(String[] args) {
        run();
    }

    private static void run() {
        SceneNode scene = createScene();

        ViewerFrame sceneViewer = new ViewerFrame(scene);
        sceneViewer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private static SceneNode createScene() {
        SceneNode scene = new SceneNode(null);

        modelNode = new ModelNode(scene);
        scene.addChild(modelNode);
        scene.setModel(modelNode);

        modelNode.setModel(new Geometry());

        camera = scene.createCameraNode();
        camera.translate(0, 0, -5);

        return scene;
    }
}