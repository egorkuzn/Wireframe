package ru.nsu.fit.g20204.kuznetsov.wireframe.volume.scene_viewer

import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.JToolBar

class ToolBar(actions: HashMap<String, ActionListener>) : JToolBar() {
    init {
        this.isFloatable = false
        val openButton = ToolButton("icon/open.png" ,"Open scene")
        this.add(openButton)
        openButton.addActionListener(actions["Open"])
        val saveButton = ToolButton("icon/save.png", "Save scene")
        this.add(saveButton)
        saveButton.addActionListener(actions["Save"])
        val editorButton = ToolButton("icon/edit.png","BSpline editor")
        this.add(editorButton)
        editorButton.addActionListener(actions["BSpline editor"])
        val normalizeViewButton = ToolButton("icon/normalise.png", "Normalize view")
        this.add(normalizeViewButton)
        normalizeViewButton.addActionListener(actions["Normalize view"])
    }
}
