package ru.nsu.fit.g20204.kuznetsov.wireframe.math;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * В данном коде описана алгебра B-сплайна
 */
public class BSpline {
    private static final int DEFAULT_SEGMENT_SIZE = 10;
    /**
     * @splineMatrix - матрица B-сплайна
     */
    private static final Matrix splineMatrix = new Matrix(
            new double[][]{
                    {-1, 3, -3, 1},
                    {3, -6, 3, 0},
                    {-3, 0, 3, 0},
                    {1, 4, 1, 0}
            }
    ).multiply(1.0 / 6);

    private List<Point2D.Double> keyPointList = new ArrayList<>();
    private List<Point2D.Double> splinePointList = new ArrayList<>();

    private int splinePointsPerSegment = DEFAULT_SEGMENT_SIZE;

    public int getSplinePointsPerSegment() {
        return splinePointsPerSegment;
    }

    public List<Point2D.Double> getKeyPoints() {
        return keyPointList;
    }

    public void addKeyPoint(Point2D.Double keyPoint) {
        keyPointList.add(keyPoint);

        evaluateSpline();
    }

    public void setKeyPoint(int index, double x, double y) {
        keyPointList.set(index, new Point2D.Double(x, y));

        evaluateSpline();
    }

    public void setKeyPointX(int dragPointIndex, double x) {
        keyPointList.get(dragPointIndex).x = x;

        evaluateSpline();
    }

    public void setKeyPointY(int dragPointIndex, double y) {
        keyPointList.get(dragPointIndex).y = y;

        evaluateSpline();
    }

    public void removeKeyPoint(int index) {
        keyPointList.remove(index);

        evaluateSpline();
    }

    /**
     * В теории i = 2 .. N - 2
     * Но т.к. счёт индексов осуществляется с 0
     * то i = 1
     */
    public void evaluateSpline() {
        splinePointList.clear();

        if (keyPointList.size() < 4)
            return;

        for (int i = 1; i < keyPointList.size() - 2; i++) {
            // Get polynomial coefficients by multiplying the spline matrix by component vectors
            Vector xCoefficients = splineMatrix.multiply(getXComponents(i), false);
            Vector yCoefficients = splineMatrix.multiply(getYComponents(i), false);
            splinePointsPerSegment(xCoefficients, yCoefficients);
        }
    }

    /**
     * У нас в модели имеется <code>keyPointList</code>, которая уже хранит
     * все управляющие точки.
     * @param i проходимся по каждой управляющей точке и берём 3 следующие.
     *          Таким образом это есть участок, в котором строится сплайн
     * @return тем самым получаем Pi .. Pi+3 вектор
     */
    private Vector getXComponents(int i) {
        return new Vector(
                keyPointList.get(i-1).x,
                keyPointList.get(i).x,
                keyPointList.get(i+1).x,
                keyPointList.get(i+2).x
        );
    }

    private Vector getYComponents(int i) {
        return new Vector(
                keyPointList.get(i-1).y,
                keyPointList.get(i).y,
                keyPointList.get(i+1).y,
                keyPointList.get(i+2).y
        );
    }

    private void splinePointsPerSegment(Vector xCoefficients, Vector yCoefficients) {
        double t;

        for (int j = 0; j < splinePointsPerSegment; j++) {
            t = (double) j /splinePointsPerSegment;

            // Coordinates of the new point
            double x = xCoefficients.x * t * t * t
                     + xCoefficients.y * t * t
                     + xCoefficients.z * t
                     + xCoefficients.w;

            double y = yCoefficients.x * t * t * t
                     + yCoefficients.y * t * t
                     + yCoefficients.z * t
                     + yCoefficients.w;

            splinePointList.add(new Point2D.Double(x, y));
        }
    }

    public List<Point2D.Double> getKeyPointList() {
        return keyPointList;
    }

    public List<Point2D.Double> getSplinePoints() {
        return splinePointList;
    }

    public void setSplinePointsPerSegment(int splinePointsPerSegment) {
        this.splinePointsPerSegment = splinePointsPerSegment;
        evaluateSpline();
    }
}
