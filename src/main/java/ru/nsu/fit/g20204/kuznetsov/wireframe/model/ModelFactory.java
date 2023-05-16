package ru.nsu.fit.g20204.kuznetsov.wireframe.model;

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.BSpline;
import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Matrix;
import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Vector;

import java.util.ArrayList;
import java.util.List;

public interface ModelFactory {
    static Geometry createRoutedSplineModel(BSpline spline, int rotationCount, int alongLayerCount, int acrossLayerCount) throws IllegalArgumentException {
        if (spline.getSplinePoints().isEmpty()) {
            return null;
        }

        if (rotationCount < alongLayerCount) {
            throw new IllegalArgumentException("alongLayerCount can't be greater than rotationCount");
        }

        if (spline.getSplinePoints().size() < acrossLayerCount) {
            throw new IllegalArgumentException("acrossLayerCount " + acrossLayerCount + " is greater than spline's point count " + spline.getSplinePoints().size());
        }

        if (alongLayerCount <= 0) {
            throw new IllegalArgumentException("alongLayerCount " + alongLayerCount + " can't be zero");
        }

        var vertexList = new ArrayList<Vector>();

        // Повернём каждую точку сплайна => получаем повенутый на угол сплайн
        // Сделаем поворот на одинаковый угол - получим вращение
        for (var point : spline.getSplinePoints()) {
            for (int i = 0; i < rotationCount; i++) {
                Matrix rotationMatrix = Matrix.getRotationMatrix(new Vector(1, 0, 0, 1), 360 * (double) i / rotationCount);
                vertexList.add(rotationMatrix.multiply(new Vector(point.x, point.y, 0, 1), true));
            }
        }

        // У нас есть сплайн с готовыми точками. Из них нужно выбрать те, которые мы будем считать
        // пересечениями с плоскостями
        var edgeList = new ArrayList<>(getSplineAcrossEdges(rotationCount, acrossLayerCount, spline.getSplinePoints().size()));

        // Как получали вершины повернутых сплайнов, так и соединяем нужные
        // Сначала идем по сплайну (возможно пропуская некоторые точки)
        // Т.е. получается некоторая ось. После получения можем переходить к отрисовке следующей
        for (int splineIndex = 0; splineIndex < spline.getSplinePoints().size() - 1; splineIndex++) {
            for (int layer = 0; layer < alongLayerCount; layer++) {
                edgeList.add(splineIndex * rotationCount + layer * rotationCount / alongLayerCount);
                edgeList.add((splineIndex + 1) * rotationCount + layer * rotationCount / alongLayerCount);
            }
        }

        return new Geometry(vertexList, edgeList);
    }


    static List<Integer> getSplineAcrossEdges(int rotationCount, int acrossLayerCount, int splineSize) {
        var edgeList = new ArrayList<Integer>();
        // Т.к. полчаемый массив точек фигуры вращения получется сначала из вращения одной точки сплайна,
        // то пересекающий сплайн первй слой будет [0; rotationCount].
        if (acrossLayerCount >= 1) {
            for (int vertexIndex = 0; vertexIndex < rotationCount; vertexIndex++) {
                edgeList.add(vertexIndex);

                if ((vertexIndex + 1) % rotationCount == 0) {
                    edgeList.add(vertexIndex - rotationCount + 1);
                } else {
                    edgeList.add(vertexIndex + 1);
                }
            }
        }

        // Для случая нескольких слоёв мы просто будем "поднимать" первый.
        if (acrossLayerCount >= 2) {
            int betweenStepSpace = (splineSize - acrossLayerCount) / (acrossLayerCount - 1);
            int extraBetweenSpace = (splineSize - acrossLayerCount) % (acrossLayerCount - 1);

            int extraCount = 0;

            for (int layer = 1; layer < acrossLayerCount; layer++) {
                if (extraCount < extraBetweenSpace) {
                    extraCount++;
                }

                for (
                        int vertexIndex = (betweenStepSpace * layer + extraCount + layer) * rotationCount;
                        vertexIndex < (betweenStepSpace * layer) * rotationCount + rotationCount;
                        vertexIndex++
                ) {
                    edgeList.add(vertexIndex);

                    if ((vertexIndex + 1) % rotationCount == 0)
                        edgeList.add(vertexIndex - rotationCount + 1);
                    else
                        edgeList.add(vertexIndex + 1);
                }
            }
        }

        return edgeList;
    }

    static Geometry createCube() {
        // Create vertices and edges
        List<Vector> vertexList = new ArrayList<>();
        List<Integer> edgeList = new ArrayList<>();

        // Create vertices
        for (double i = -1; i <= 1; i += 2) {
            for (double j = -1; j <= 1; j += 2) {
                for (double k = -1; k <= 1; k += 2) {
                    vertexList.add(new Vector(i, j, k, 1));
                }
            }
        }

        // TODO: Can't it be more elegant?
        // Connect vertices with edges
        edgeList.add(0);
        edgeList.add(1);

        edgeList.add(0);
        edgeList.add(2);

        edgeList.add(0);
        edgeList.add(4);

        edgeList.add(1);
        edgeList.add(3);

        edgeList.add(1);
        edgeList.add(5);

        edgeList.add(2);
        edgeList.add(3);

        edgeList.add(2);
        edgeList.add(6);

        edgeList.add(3);
        edgeList.add(7);

        edgeList.add(4);
        edgeList.add(5);

        edgeList.add(4);
        edgeList.add(6);

        edgeList.add(5);
        edgeList.add(7);

        edgeList.add(6);
        edgeList.add(7);

        return new Geometry(vertexList, edgeList);
    }
}
