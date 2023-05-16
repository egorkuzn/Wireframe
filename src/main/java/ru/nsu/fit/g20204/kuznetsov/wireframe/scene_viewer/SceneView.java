package ru.nsu.fit.g20204.kuznetsov.wireframe.scene_viewer;

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Matrix;
import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Vector;
import ru.nsu.fit.g20204.kuznetsov.wireframe.model.Geometry;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.CameraNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.ModelNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.Node;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.SceneNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class SceneView extends JPanel {
    Color edgeColor = new Color(0, 0 , 0);

    private SceneNode scene;
    private CameraNode camera;
    private ModelNode model;

    private Point rotateScreenOrigin;
    private static final double rotationSpeed = 3;
    private static final double zoomSpeed = 0.1;

    public SceneView(SceneNode scene, CameraNode camera) {
        this.scene = scene;
        this.camera = camera;
        this.model = scene.getModel();

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
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addMouseWheelListener(mouseAdapter);

        repaint();
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

    public void mouseWheelMoved(MouseWheelEvent e) {
        double offset = e.getPreciseWheelRotation() * zoomSpeed;

        if (camera.getNearClippingPlane() <= 0 && offset >= 0)
            return;

        camera.setNearClippingPlane(camera.getNearClippingPlane() - offset);
        camera.translate(0, 0, offset);

        this.repaint();
    }

    private void paintEdges(Graphics2D g2d, List<Vector> viewPortVertices, List<Integer> edgeList) {
        for (int i = 0; i < edgeList.size() / 2; i++) {
            Vector viewPortPoint1 = viewPortVertices.get(edgeList.get(2 * i));
            Vector viewPortPoint2 = viewPortVertices.get(edgeList.get(2 * i + 1));


            if (viewPortPoint1.z <= 0 || viewPortPoint1.z > 1 || viewPortPoint2.z <= 0 || viewPortPoint2.z > 1) {
                continue;
            }

            Point screenPoint1 = getScreenPoint(viewPortPoint1);
            Point screenPoint2 = getScreenPoint(viewPortPoint2);

            Color color = getColorByDistance(edgeColor, Math.min(viewPortPoint1.z, viewPortPoint2.z));

            g2d.setColor(color);
            g2d.setStroke(new BasicStroke((int) (5 * (1 - Math.min(viewPortPoint1.z, viewPortPoint2.z))), BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));

            g2d.drawLine(screenPoint1.x, screenPoint1.y, screenPoint2.x, screenPoint2.y);
        }
    }

    public void setScene(SceneNode scene, CameraNode camera) {
        this.scene = scene;
        this.camera = camera;
        this.model = scene.getModel();
    }

    private Point getScreenPoint(Vector viewPortPoint) {
        int x = (int)((viewPortPoint.x) * this.getWidth() / camera.getViewPortWidth()) + this.getWidth() / 2;
        int y = (int)((viewPortPoint.y) * this.getHeight() / camera.getViewPortHeight()) + this.getHeight() / 2;

        return new Point(x, y);
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        if (this.getWidth() < this.getHeight()) {
            camera.setViewPortWidth(1.5);
            camera.setViewPortHeight(1.5 * getHeight() / getWidth());
        } else {
            camera.setViewPortHeight(1.5);
            camera.setViewPortWidth(1.5 * getWidth() / getHeight());
        }

        BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D imageGraphics = (Graphics2D) bufferedImage.getGraphics();
        imageGraphics.setColor(new Color(169, 169, 169));
        imageGraphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

        paintNode(bufferedImage, scene);

        g2d.drawImage(bufferedImage, (getWidth() - bufferedImage.getWidth())/ 2, (getHeight() - bufferedImage.getHeight())/ 2, this);

        g2d.setColor(Color.WHITE);
        g2d.drawRect (
                (getWidth() - bufferedImage.getWidth())/2,
                (getHeight() - bufferedImage.getHeight())/2,
                bufferedImage.getWidth() - 1,
                bufferedImage.getHeight() - 1
        );
    }

    private void paintNode(BufferedImage bufferedImage, Node node) {
        if (node instanceof ModelNode modelNode) {
            Matrix modelNodeGlobalTransform = modelNode.getGlobalTransform();
            Matrix modelScaleTransform = modelNode.getBoundBoxMatrix();
            Matrix cameraGlobalTransform = camera.getGlobalTransform();
            Matrix cameraViewportTransform = camera.getViewportTransform();

            Matrix projectionMatrix = cameraViewportTransform.multiply(
                    cameraGlobalTransform.multiply(
                            modelScaleTransform.multiply(
                                    modelNodeGlobalTransform)));

            Geometry geometry = modelNode.getModel();

            Graphics2D g =  (Graphics2D) bufferedImage.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Vector center = new Vector(0, 0, 0, 1);
            Vector viewPortCenter = projectionMatrix.multiply(center, true);
            Point screenPointCenter = getScreenPoint(viewPortCenter);

            BiFunction<Vector, Color, Void> paintAxis = (Vector vector, Color color) -> {
                g.setColor(color);
                Vector viewPortAxis = projectionMatrix.multiply(vector, true);
                Point screenPointAxis = getScreenPoint(viewPortAxis);
                g.drawLine (
                        screenPointCenter.x,
                        screenPointCenter.y,
                        screenPointAxis.x,
                        screenPointAxis.y
                );
                return null;
            };

            paintAxis.apply(new Vector(1, 0, 0, 1), Color.RED);
            paintAxis.apply(new Vector(0, 1, 0, 1), Color.GREEN);
            paintAxis.apply(new Vector(0, 0, 1, 1), Color.BLUE);

            List<Vector> viewPortVertices = new ArrayList<>();

            for (var vertex: geometry.getVertexList()) {
                viewPortVertices.add(projectionMatrix.multiply(vertex, true));
            }

            paintEdges(g, viewPortVertices, geometry.getEdgeList());
        }

        for (var childNode: node.getChildNodes()) {
            paintNode(bufferedImage, childNode);
        }
    }
}
