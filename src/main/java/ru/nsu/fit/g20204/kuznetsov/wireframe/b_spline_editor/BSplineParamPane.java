package ru.nsu.fit.g20204.kuznetsov.wireframe.b_spline_editor;

import javax.swing.*;
import java.awt.*;

public class BSplineParamPane extends JPanel {
    private final BSplinePane splinePane;

    private final SpinnerNumberModel splinePointsPerSegmentSpinnerModel = new SpinnerNumberModel(
            10,
            2,
            100,
            1
    );

    private final SpinnerNumberModel acrossLayersSpinnerModel = new SpinnerNumberModel(
            0,
            0,
            0,
            1
    );


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

        // Spline points per segment
        JSpinner splinePointsPerSegmentSpinner = new JSpinner(splinePointsPerSegmentSpinnerModel);
        splineParametersPane.add(getSpinnerPane("Points per spline segment", splinePointsPerSegmentSpinner));

        splinePointsPerSegmentSpinner.addChangeListener(l -> {
            splinePane.setSplinePointsPerSegment((int) splinePointsPerSegmentSpinnerModel.getValue());

            acrossLayersSpinnerModel.setMaximum(splinePane.getSpline().getSplinePoints().size());
            acrossLayersSpinnerModel.setValue(Math.min((Integer) acrossLayersSpinnerModel.getNumber(), (Integer) acrossLayersSpinnerModel.getMaximum()));
        });

        return splineParametersPane;
    }

    private Component getSpinnerPane(String name, JSpinner spinner) {
        JPanel pane = new JPanel();
        pane.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(name + ": ");
        pane.add(label);
        pane.add(spinner);
        return pane;
    }

    private JPanel getKeyPointParametersPane() {
        return null;
    }

    private JPanel getSplineParameterPane() {
        return null;
    }
}
