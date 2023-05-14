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
    private static final int INDENT_STEP_ON_DEFAULT_ZOOM = 64;

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

    private int pixelsPerIndentStep = INDENT_STEP_ON_DEFAULT_ZOOM;
    private double zoom = 100;
    private int verticalOffset = 0;
    private int horizontalOffset = 0;

    private Point dragOrigin;
    private int dragPointIndex;

    List<BiFunction<Integer, Point2D.Double, Void>> pointModifiedListener = new ArrayList<>();

    private final BSpline spline = new BSpline();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        paintAxes(g2d);
        paintSpline(g2d);
    }
    
    private int verticalPosition;
    private int horizontalPosition;
    
    private void paintAxes(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(axisSize));

        verticalPosition = this.getWidth() / 2 + verticalOffset;
        horizontalPosition = this.getWidth() / 2 + horizontalOffset;
        
        paintXAxes(g2d);
        paintYAxes(g2d);
    }
    
    private void paintXAxes(Graphics2D g2d) {
        g2d.setColor(xAxisColor);
        g2d.drawLine(0, verticalPosition, this.getWidth() - 1, verticalPosition);
        
        for (int i = horizontalPosition + pixelsPerIndentStep; i <= this.getWidth(); i += pixelsPerIndentStep) {
            g2d.drawLine(i, verticalPosition - axisSize * 2,i, verticalPosition + axisSize * 2);
        }
        
        for (int i = horizontalPosition - pixelsPerIndentStep; i > 0; i -= pixelsPerIndentStep) {
            g2d.drawLine(i, verticalPosition - axisSize * 2,i, verticalPosition + axisSize * 2);
        }
    }
    
    private void paintYAxes(Graphics2D g2d) {
        g2d.setColor(yAxisColor);
        g2d.drawLine(horizontalPosition, 0, horizontalPosition, this.getHeight() - 1);
        
        for (int i = verticalPosition + pixelsPerIndentStep; i <= this.getHeight(); i += pixelsPerIndentStep) {
            g2d.drawLine(horizontalPosition + axisSize * 2, i,horizontalPosition - axisSize * 2, i);
        }
        for (int i = verticalPosition - pixelsPerIndentStep; i > 0; i -= pixelsPerIndentStep) {
            g2d.drawLine(horizontalPosition + axisSize * 2, i,horizontalPosition - axisSize * 2, i);
        }
    }
    
    private void paintSpline(Graphics2D g2d) {
        if (spline.getKeyPoints().isEmpty())
    }
}
