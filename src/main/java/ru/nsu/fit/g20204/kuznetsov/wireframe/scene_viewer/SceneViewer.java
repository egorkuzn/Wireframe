package ru.nsu.fit.g20204.kuznetsov.wireframe.scene_viewer;

import ru.nsu.fit.g20204.kuznetsov.wireframe.node.CameraNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.ModelNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.SceneNode;

import javax.swing.*;

public class SceneViewer extends JPanel {
    private SceneNode scene;
    private CameraNode camera;
    private ModelNode model;

    public SceneViewer(SceneNode scene, CameraNode camera) {
        this.scene = scene;
        this.camera = camera;
    }
}
