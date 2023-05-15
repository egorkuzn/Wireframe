package ru.nsu.fit.g20204.kuznetsov.wireframe.scene_viewer;

import ru.nsu.fit.g20204.kuznetsov.wireframe.node.SceneNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.util.ModelParser;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileChooser {
    private JFrame parentFrame;

    public FileChooser(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public SceneNode showOpenDialog() {
        FileDialog fileDialog = new FileDialog(parentFrame, "Open scene", FileDialog.LOAD);
        fileDialog.setFile("*.icg");
        fileDialog.setVisible(true);

        if(fileDialog.getFile() != null) {
            return ModelParser.fileToScene(fileDialog.getFiles()[0]);
        }

        return null;
    }

    public void showSaveDialog(SceneNode scene) {
        FileDialog fileDialog = new FileDialog(parentFrame, "Save scene", FileDialog.SAVE);
        fileDialog.setFile("*.graphics");
        fileDialog.setVisible(true);

        if (fileDialog.getFile() != null) {
            File file = fileDialog.getFiles()[0];

            // Add extension if needed
            if (!file.getName().endsWith(".graphics"))
                file = new File(file.getPath() + ".graphics");

            boolean saved = ModelParser.sceneToFile(scene, file);
            if (!saved) {
                JOptionPane.showMessageDialog(parentFrame, "Failed to save file!");
            }
        }
    }
}
