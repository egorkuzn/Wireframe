package ru.nsu.fit.g20204.kuznetsov.wireframe.volume.scene_viewer

import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.JToolBar

class ToolBar(actions: HashMap<String, ActionListener>) : JToolBar() {
    init {
        this.isFloatable = false
        val openButton = JButton("Open scene")
        this.add(openButton)
        openButton.addActionListener(actions["Open"])
        val saveButton = JButton("Save scene")
        this.add(saveButton)
        saveButton.addActionListener(actions["Save"])
        val editorButton = JButton("BSpline editor")
        this.add(editorButton)
        editorButton.addActionListener(actions["BSpline editor"])
        val normalizeViewButton = JButton("Normalize view")
        this.add(normalizeViewButton)
        normalizeViewButton.addActionListener(actions["Normalize view"])
    }
}
