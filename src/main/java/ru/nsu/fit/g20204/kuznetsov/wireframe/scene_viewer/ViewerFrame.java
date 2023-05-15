package ru.nsu.fit.g20204.kuznetsov.wireframe.scene_viewer;

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Matrix;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.SceneNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class ViewerFrame extends JFrame {
    private HashMap<String, ActionListener> actions;
    private FileChooser fileChooser = new FilewChooser(this);

    private SceneView sceneView;
    private SceneNode scene;

    public SceneViewerFrame(SceneNode sceneNode) {
        super("3D Scene Viewer");

        this.scene = sceneNode;


        this.setMinimumSize(new Dimension(640, 480));
        this.setLocation(400, 160);
        this.setVisible(true);

        actions = new HashMap<>() {{
            put("Normalize view", e -> {
                scene.getModelNode().setLocalTransform(new Matrix());
                repaint();
            });
            put("BSpline editor", e -> {
                BSplineEditor splineEditor = new BSplineEditor();
                splineEditor.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        scene.getModelNode().setModel(splineEditor.getSplineModel());
                        repaint();
                    }
                });
                splineEditor.addSplineModelChangedListener(m -> {
                    scene.getModelNode().setModel(m);
                    repaint();
                    return null;
                });
            });
            put("Save", e -> {
                fileChooser.showSaveDialog(scene);
            });
            put("Open", e -> {
                SceneNode scene = fileChooser.showOpenDialog();
                if (scene != null)
                    setScene(scene, scene.getCameraList().get(0));
            });
        }};
    }
}
