package ru.nsu.fit.g20204.kuznetsov.wireframe.node;

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Matrix;
import ru.nsu.fit.g20204.kuznetsov.wireframe.model.Geometry;

public class ModelNode extends Node {
    private Geometry model;

    public ModelNode (Node parentNode) {
        super(parentNode);
    }

    public void setModel(Geometry model) {
        if (model != null) {
            this.model = model;
        }
    }

    public Geometry getModel() {
        return model;
    }

    // Получаем матрицу, чтобы моделька вмещалась в бокс
    public Matrix getBoundBoxMatrix() {
        double xMax= model.getVertexList().get(0).x;
        double yMax = model.getVertexList().get(0).y;
        double zMax = model.getVertexList().get(0).z;

        double xMin = model.getVertexList().get(0).x;
        double yMin = model.getVertexList().get(0).y;
        double zMin = model.getVertexList().get(0).z;

        for (var vertex: model.getVertexList()) {
            xMax = Math.max(vertex.x, xMax);
            xMin = Math.min(vertex.x, xMin);

            yMax = Math.max(vertex.y, yMax);
            yMin = Math.min(vertex.y, yMin);

            zMax = Math.max(vertex.z, zMax);
            zMin = Math.min(vertex.z, zMin);
        }

        double scale = Math.max(Math.max(xMax - xMin, yMax - yMin), zMax - zMin);

        return Matrix.getScaleMatrix(1 / scale, 1 / scale, 1 / scale);
    }
}
