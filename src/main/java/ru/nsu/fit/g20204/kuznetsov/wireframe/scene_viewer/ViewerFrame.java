package ru.nsu.fit.g20204.kuznetsov.wireframe.scene_viewer;

import ru.nsu.fit.g20204.kuznetsov.wireframe.b_spline_editor.BSplineEditor;
import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Matrix;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.CameraNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.SceneNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import static java.awt.BorderLayout.PAGE_START;

public class ViewerFrame extends JFrame {
    private HashMap<String, ActionListener> actions;
    private FileChooser fileChooser = new FileChooser(this);

    private SceneView sceneView;
    private SceneNode scene;

    public ViewerFrame(SceneNode sceneNode) {
        super("3D Scene Viewer");

        scene = sceneNode;


        setMinimumSize(new Dimension(640, 480));
        setLocation(400, 160);
        setVisible(true);

        actions = new HashMap<>() {{
            put("Normalize view", e -> {
                scene.getModel().setLocalTransformMatrix(new Matrix());
                repaint();
            });
            put("BSpline editor", e -> {
                BSplineEditor splineEditor = new BSplineEditor();
                splineEditor.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        scene.getModel().setModel(splineEditor.getSplineModel());
                        repaint();
                    }
                });
                splineEditor.addSplineModelChangeListener(m -> {
                    scene.getModel().setModel(m);
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

        var camera = scene.getCameraList().get(0);
        sceneView = new SceneView(scene, camera);
        add(sceneView);

        var toolBar = new ToolBar(actions);
        add(toolBar, PAGE_START);

        var menuBar = new MenuBar(actions);
        setJMenuBar(menuBar);

        setScene(scene, camera);
        pack();
    }

    public void setScene(SceneNode scene, CameraNode camera) {
        this.scene = scene;
        sceneView.setScene(scene, camera);
        this.repaint();
    }
}
