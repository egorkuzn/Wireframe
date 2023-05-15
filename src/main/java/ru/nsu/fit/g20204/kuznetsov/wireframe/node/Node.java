package ru.nsu.fit.g20204.kuznetsov.wireframe.node;

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Matrix;
import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Vector;

import java.util.LinkedList;
import java.util.List;

public class Node {
    private final Node parentNode;
    private final List<Node> childNodes = new LinkedList<>();
    private Matrix localTranformMatrix = new Matrix();

    public Node(Node parentNode) {
        this.parentNode = parentNode;
    }

    public void addChild(Node node) {
        childNodes.add(node);
    }

    public Node getParent() {
        return parentNode;
    }

    public List<Node> getChildNodes() {
        return childNodes;
    }

    public Matrix getLocalTranformMatrix() {
        return localTranformMatrix;
    }

    public Matrix getGlobalTransform() {
        if (parentNode == null) {
            return  localTranformMatrix;
        }

        return parentNode.getGlobalTransform().multiply(localTranformMatrix);
    }
    public void translate(double dx, double dy, double dz) {
        localTranformMatrix = localTranformMatrix.translate(dx, dy, dz);
    }

    public void scale(double xScale, double yScale, double zScale) {
        localTranformMatrix = localTranformMatrix.scale(xScale, yScale, zScale);
    }

    public void rotate(Vector axis, double angle) {
        this.localTranformMatrix = localTranformMatrix.rotate(axis, angle);
    }

    public void setLocalTranformMatrix(Matrix matrix) {
        this.localTranformMatrix = matrix;
    }
}
