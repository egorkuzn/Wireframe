package ru.nsu.fit.g20204.kuznetsov.wireframe.node;

import java.util.LinkedList;
import java.util.List;

public class Node {
    private final Node parentNode;

    private final List<Node> childNodes = new LinkedList<>();

    public Node(Node parentNode) {
        this.parentNode = parentNode;
    }

    public void addChild(Node node) {
        childNodes.add(node);
    }

    public Node getParent() {
        return parentNode;
    }
}
