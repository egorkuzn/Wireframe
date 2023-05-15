package ru.nsu.fit.g20204.kuznetsov.wireframe.model;

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.BSpline;

public interface ModelFactory {
    static Geometry createRoatedSplineModel(BSpline spline, int rotationCount, int alongLayerCount, int acreossLayerCount) throws IllegalArgumentException {
        if (spline.getSplinePints().isEmpty()) {
            return null;
        }

        if (rotationCount < alongLayerCount) {
            throw new IllegalArgumentException("");
        }

        if (spline.getSplinePoints().size() < acreossLayerCount) {
            throw new IllegalArgumentException("");
        }

        if (alongLayerCount <= 0) {
            throw new IllegalArgumentException("")
        }
    }
}
