package ru.nsu.fit.g20204.kuznetsov.wireframe.volume.scene_viewer

import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import javax.swing.*

class MenuBar internal constructor(private val actions: HashMap<String, ActionListener>) : JMenuBar() {
    init {

        // File menu
        this.add(fileMenu)

        // Edit menu
        this.add(editMenu)

        // View menu
        this.add(viewMenu)

        // About menu
        this.add(this.helpMenu)
    }

    private val fileMenu: JMenu
        private get() {
            val fileMenu = JMenu("File")
            val openItem = JMenuItem("Open")
            fileMenu.add(openItem)
            val saveItem = JMenuItem("Save")
            fileMenu.add(saveItem)
            return fileMenu
        }
    private val editMenu: JMenu
        private get() {
            val editMenu = JMenu("Edit")
            val splineEditorItem = JMenuItem("B-Spline Editor")
            splineEditorItem.addActionListener(actions["BSpline editor"])
            editMenu.add(splineEditorItem)
            return editMenu
        }
    private val viewMenu: JMenu
        private get() {
            val viewMenu = JMenu("View")
            val normalizeViewItem = JMenuItem("Normalize view")
            normalizeViewItem.addActionListener(actions["Normalize view"])
            viewMenu.add(normalizeViewItem)
            return viewMenu
        }

    override fun getHelpMenu(): JMenu {
        val helpMenu = JMenu("Help")
        val aboutItem = JMenuItem("About")
        aboutItem.addActionListener { e: ActionEvent? -> showAboutMessage() }
        helpMenu.add(aboutItem)
        return helpMenu
    }

    private fun showAboutMessage() {
        val aboutText = JTextArea()
        aboutText.isEditable = false
        aboutText.lineWrap = true
        aboutText.wrapStyleWord = true
        aboutText.border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
        try {
            val aboutFile = InputStreamReader(FileInputStream("src/main/resources/about.txt"), StandardCharsets.UTF_8)
            aboutText.read(aboutFile, "txt")
        } catch (e: IOException) {
            aboutText.text = "Failed to load about.txt"
        }
        val aboutTextScroll = JScrollPane(aboutText)
        aboutTextScroll.preferredSize = Dimension(400, 200)
        JOptionPane.showMessageDialog(parent, aboutTextScroll, "About", JOptionPane.INFORMATION_MESSAGE)
    }
}
