package ru.nsu.fit.g20204.kuznetsov.wireframe.b_spline_editor;

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.BSpline;
import ru.nsu.fit.g20204.kuznetsov.wireframe.model.Geometry;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BSplineEditor extends JFrame {
    private final BSplinePane splinePane = new BSplinePane();
    private final BSplineParamPane paramPane = new BSplineParamPane(splinePane, this);
    private final List<Function<Geometry, Void>> splineChangedListeners = new ArrayList<>();


    public BSplineEditor() {
        super("B-Spline Editor");

        this.setMinimumSize(new Dimension(640, 480));
        this.setLocation(600, 160);
        this.setLocation(600, 160);
        this.setVisible(true);

        this.add(splinePane, BorderLayout.CENTER);
        this.add(paramPane, BorderLayout.PAGE_END);
    }

    public BSpline getSpline() {
        return splinePane.getSpline();
    }

    public Geometry getSplineModel() {
        return paramPane.getSplineModel();
    }

    public void addSplineModelChangeListener(Function<Geometry, Void> listener) {
        splineChangedListeners.add(listener);
    }

    public void applySpline() {
        for (var l : splineChangedListeners) {
            l.apply(paramPane.getSplineModel());
        }
    }
}
