package ru.nsu.fit.g20204.kuznetsov.wireframe.scene_viewer;

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Vector;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.CameraNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.ModelNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.SceneNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

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
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                sceneView.mouseWheelMoved(e);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        rotateScreenOrigin = e.getPoint();
    }

    public void mouseDragged(MouseEvent e) {
        if (rotateScreenOrigin == null) {
            rotateScreenOrigin = e.getPoint();
            return;
        }


        Point screenAxis = new Point(e.getX() - rotateScreenOrigin.x, e.getY() - rotateScreenOrigin.y);
        rotateScreenOrigin = e.getPoint();


        if (screenAxis.x == 0 && screenAxis.y == 0) {
            return;
        }

        var focusNodeAxis = new Vector(screenAxis.y, -screenAxis.x, 0, 1);
        model.rotate(focusNodeAxis, rotationSpeed);

        this.repaint();
    }

//    public void mouseWheelMoved(MouseWheelEvent e) {
//        double offset = e.getPreciseWheelRotation() * zoomSpeed;
//
//        if (camera.getNearClipingPlane(camera.getNearClippingPlane() - offset))
//    }

    private void paintEdges(Graphics2D g2d, ArrayList<Vector> viewPortVertices, ArrayList<Integer> edgeList) {
        for (int i = 0; i < edgeList.size() / 2; i++) {
            Vector viewPortPoint1 = viewPortVertices.get(edgeList.get(2 * i));
            Vector viewPortPoint2 = viewPortVertices.get(edgeList.get(2 * i + 1));


            if (viewPortPoint1.z <= 0 || viewPortPoint1.z > 1 || viewPortPoint2.z <= 0 || viewPortPoint2.z > 1) {
                continue;
            }

            Point screenPoint1 = getScreenPoint(viewPortPoint1);
            Point screenPoint2 = getScreenPoint(viewPortPoint2);

            Color = getColorByDistance(edgeColor, Math.min(viewPortPoint1.z, viewPortPoint2.z));

            g2d.setColor(color);
            g2d.setStroke(new BasicStroke((int) (5 * (1 - Math.min(viewPortPoint1.z, viewPortPoint2.z))), BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));

            g2d.drawLine(screenPoint1.x, screenPoint1.y, screenPoint2.x, screenPoint2.y);
        }
    }

    public void setScene(SceneNode scene, CameraNode camera) {
        this.scene = scene;
        this.camera = camera;
        this.model = scene.getModelNode();
    }

    private Point getScreenPoint() {
        return
    }

    private Color getColorByDistance(Color color, double distance) {
        distance = 1 - distance;

        int rgb = color.getRGB();

        int red = Math.min(Math.max((int)(((rgb >> 16) & 0xFF) * distance), 0), 255);
        int green = Math.min(Math.max((int)(((rgb >> 8) & 0xFF) * distance), 0), 255);
        int blue = Math.min(Math.max((int)((rgb & 0xFF) * distance), 0), 255);

        int newRgb = (0xFF << 24) | (red << 16) | (green << 8) | blue;

        return new Color(newRgb);
    }
}
