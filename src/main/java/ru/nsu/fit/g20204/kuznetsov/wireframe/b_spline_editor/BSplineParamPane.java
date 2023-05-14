package ru.nsu.fit.g20204.kuznetsov.wireframe.b_spline_editor;

import javax.swing.*;
import java.awt.*;

public class BSplineParamPane extends JPanel {
    private final BSplinePane splinePane;


    public BSplineParamPane(BSplinePane splinePane) {
        super();
        this.splinePane = splinePane;
        addAll();
    }

    private void addAll() {
        add(getSplineParametrPane());
        add(getKeyPointParamentersPane());
        add(getModelParametersPane());
        add(getApplyButton());
    }

    private Component getApplyButton() {
        return null;
    }

    private Component getModelParametersPane() {
        return null;
    }

    private Component getKeyPointParamentersPane() {
        return null;
    }

    private Component getSplineParametrPane() {
        return null;
    }
}
