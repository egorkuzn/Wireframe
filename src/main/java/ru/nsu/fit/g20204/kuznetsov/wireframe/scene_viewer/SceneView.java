package ru.nsu.fit.g20204.kuznetsov.wireframe.scene_viewer;

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Vector;
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
        // If origin of rotation wasn't set, set it and return
        if (rotateScreenOrigin == null) {
            rotateScreenOrigin = e.getPoint();
            return;
        }

        // Get mouse movement vector
        Point screenAxis = new Point(e.getX() - rotateScreenOrigin.x, e.getY() - rotateScreenOrigin.y);

        // Set new origin point
        rotateScreenOrigin = e.getPoint();

        // If vector is zero, return
        if (screenAxis.x == 0 && screenAxis.y == 0) {
            return;
        }

        // Set vector to rotate the model around and rotate it
        var focusNodeAxis = new Vector(screenAxis.y, -screenAxis.x, 0, 1);
        model.rotate(focusNodeAxis, rotationSpeed);

        this.repaint();
    }


}
