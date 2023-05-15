package ru.nsu.fit.g20204.kuznetsov.wireframe.scene_viewer;

import ru.nsu.fit.g20204.kuznetsov.wireframe.node.CameraNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.ModelNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.SceneNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SceneView extends JPanel {
    private SceneNode scene;
    private CameraNode camera;
    private ModelNode model;

    private Point rotateScreenOrigin;
    private double rotationSpeed = 3;
    private double zoomSpeed = 0.1;

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
            @Override
            public void mouseDragged(MouseEvent e) {
                sceneView.mouseDragged(e);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        rotateScreenOrigin = e.getPoint();
    }

    public void mouseDragged(MouseEvent e) {

    }


}
