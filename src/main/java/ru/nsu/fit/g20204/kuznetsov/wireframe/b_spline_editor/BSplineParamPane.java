package ru.nsu.fit.g20204.kuznetsov.wireframe.b_spline_editor;

import javax.swing.*;
import java.awt.*;

public class BSplineParamPane extends JPanel {
    private final BSplinePane splinePane;


    public BSplineParamPane(BSplinePane splinePane, BSplineEditor splineEditor) {
        super();
        this.splinePane = splinePane;
        addAll(splineEditor);
    }

    private void addAll(BSplineEditor splineEditor) {
        add(getSplineParameterPane());
        add(getKeyPointParametersPane());
        add(getModelParametersPane());
        add(getApplyButton(splineEditor));
    }

    private JButton getApplyButton(BSplineEditor splineEditor) {
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> splineEditor.applySpline());
        return applyButton;
    }

    private JPanel getModelParametersPane() {
        JPanel splineParametersPane = new JPanel();
        splineParametersPane.setLayout(new BoxLayout(splineParametersPane, BoxLayout.PAGE_AXIS));



        return splineParametersPane;
    }

    private JPanel getKeyPointParametersPane() {
        return null;
    }

    private JPanel getSplineParameterPane() {
        return null;
    }
}
