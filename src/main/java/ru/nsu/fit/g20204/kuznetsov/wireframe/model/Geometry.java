package ru.nsu.fit.g20204.kuznetsov.wireframe.model;

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Vector;

import java.util.ArrayList;
import java.util.List;

public class Geometry {
    private final List<Vector> vectorList;
    private final List<Integer> edgeList;

    public Geometry() {
        vectorList = new ArrayList<>();
        edgeList = new ArrayList<>();
        vectorList.add(new Vector());
    }

    public Geometry(List<Vector> vertexList, List<Integer> edgeList) {
        this.vectorList = vertexList;
        this.edgeList = edgeList;
    }

    public List<Vector> getVertexList() {
        return vectorList;
    }

    public List<Integer> getEdgeList() {
        return edgeList;
    }
}
