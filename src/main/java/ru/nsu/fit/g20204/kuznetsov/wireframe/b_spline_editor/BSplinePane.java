package ru.nsu.fit.g20204.kuznetsov.wireframe.b_spline_editor;

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.BSpline;

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

    private Color backgroundColor = new Color(169, 169, 169);
    private Color xAxisColor = new Color(51, 51, 51);
    private Color yAxisColor = new Color(51, 51, 51);
    private Color keyPointColor = new Color(42, 122, 255);
    private Color selectedKeyPointColor = new Color(255, 0, 0);
    private Color brokenLineColor = new Color(42, 46, 54);
    private Color splineColor = new Color(94, 255, 0);

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
        if (spline.getKeyPoints().isEmpty()) {
            return;
        }

        connectPointsWithStraightLines(g2d);
        showKeyPoints(g2d);
        showSpline(g2d);
    }

    private void connectPointsWithStraightLines(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(brokenLineSize));
        g2d.setColor(brokenLineColor);
        Point previousScreenKeyPoint = getPointOnScreen(spline.getKeyPoints().get(0));

        for (var point: spline.getKeyPoints()) {
            Point screenPoint = getPointOnScreen(point);

            g2d.drawLine(previousScreenKeyPoint.x, previousScreenKeyPoint.y, screenPoint.x, screenPoint.y);

            previousScreenKeyPoint = screenPoint;
        }
    }

    private Point getPointOnScreen(Point2D.Double point) {
        return getPointOnScreen(point.getX(), point.getY());
    }

    private Point getPointOnScreen(double x, double y) {
        int xPixelDistance = (int) (x * pixelsPerIndentStep);
        int yPixelDistance = (int) (-y * pixelsPerIndentStep);

        int xAbsolutePos = xPixelDistance + (this.getWidth() / 2 + horizontalOffset);
        int yAbsolutePos = yPixelDistance + (this.getHeight() / 2 + verticalOffset);

        return new Point(xAbsolutePos, yAbsolutePos);
    }

    private Point2D.Double getContinuousPoint(int x, int y) {
        int xPixelDistance = x - (this.getWidth() / 2 + horizontalOffset);
        int yPixelDistance = y - (this.getHeight() / 2 + verticalOffset);

        double xContinuous = (double) xPixelDistance / pixelsPerIndentStep;
        double yContinuous = (double) yPixelDistance / pixelsPerIndentStep;

        return new Point2D.Double(xContinuous, -yContinuous);
    }

    private void showKeyPoints(Graphics2D g2d) {
        g2d.setColor(keyPointColor);

        for (int keyPointIndex = 0; keyPointIndex < spline.getKeyPoints().size(); keyPointIndex++) {
            Point2D.Double keyPoint = spline.getKeyPoints().get(keyPointIndex);

            Point screenPoint = this.getPointOnScreen(keyPoint.x, keyPoint.y);

            if (keyPointIndex == dragPointIndex)
                g2d.setColor(selectedKeyPointColor);
            else
                g2d.setColor(keyPointColor);
            g2d.drawOval(screenPoint.x - pointRadius, screenPoint.y - pointRadius, pointRadius * 2, pointRadius * 2);
        }
    }

    private void showSpline(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(splineSize));
        g2d.setColor(splineColor);
        var splinePoints = spline.getSplinePoints();

        if (!splinePoints.isEmpty()) {
            Point previousScreenSplinePoint = getPointOnScreen(splinePoints.get(0));
            for (var point : splinePoints) {
                Point screenPoint = getPointOnScreen(point);

                g2d.drawLine(previousScreenSplinePoint.x, previousScreenSplinePoint.y, screenPoint.x, screenPoint.y);

                previousScreenSplinePoint = screenPoint;
            }
        }
    }
}
