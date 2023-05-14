package ru.nsu.fit.g20204.kuznetsov.wireframe.b_spline_editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class BSplinePane extends JPanel implements MouseWheelListener, MouseMotionAdapter, MouseAdapter {
    public BSplinePane() {
        super();
        this.addMouseListener(this);
        this.addMouseWheelListener(this);
        this.addMouseMotionListener(this);
    }

    private Color backgroundColor = new Color(45, 45, 51);
    private Color xAxisColor = new Color(255, 255, 255);
    private Color yAxisColor = new Color(255, 255, 255);
    private Color keyPointColor = new Color(0, 0, 0);
    private Color selectedKeyPointColor = new Color(220, 12, 12);
    private Color brokenLineColor = new Color(255, 255, 0);
    private Color splineColor = new Color(77, 255, 0);

    private int axisSize = 2;
    private int splineSize = 2;
    private int brokenLineSize = 1;
    private int pointRadius = 10;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

    }
}
