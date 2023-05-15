package ru.nsu.fit.g20204.kuznetsov.wireframe.scene_viewer;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class ToolBar extends JToolBar {
    public ToolBar(HashMap<String, ActionListener> actions) {
        super();
        this.setFloatable(false);

        JButton openButton = new JButton("Open scene");
        this.add(openButton);
        openButton.addActionListener(actions.get("Open"));

        JButton saveButton = new JButton("Save button");
        this.add(saveButton);
        saveButton.addActionListener(actions.get("Save"));

        JButton editorButton = new JButton("BSpline editor");
        this.add(editorButton);
        editorButton.addActionListener(actions.get("BSpline editor"));

        JButton normalizeViewButton = new JButton("Normalize view");
        this.add(normalizeViewButton);
        normalizeViewButton.addActionListener(actions.get("Normalize view"));
    }
}
