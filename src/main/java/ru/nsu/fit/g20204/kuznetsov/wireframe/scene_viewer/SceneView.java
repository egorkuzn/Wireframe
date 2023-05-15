package ru.nsu.fit.g20204.kuznetsov.wireframe.scene_viewer;

import ru.nsu.fit.g20204.kuznetsov.wireframe.node.CameraNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.ModelNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.SceneNode;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SceneView extends JPanel {
    private SceneNode scene;
    private CameraNode camera;
    private ModelNode model;

    public SceneView(SceneNode scene, CameraNode camera) {
        this.scene = scene;
        this.camera = camera;
        this.focusNode = scene.getModelNode();

        SceneView sceneView = this;
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                sceneView.mousePressed(e);
            }
        }
    }
}
