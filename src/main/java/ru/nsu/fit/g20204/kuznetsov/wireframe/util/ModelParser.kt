package ru.nsu.fit.g20204.kuznetsov.wireframe.util;

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Matrix;
import ru.nsu.fit.g20204.kuznetsov.wireframe.math.Vector;
import ru.nsu.fit.g20204.kuznetsov.wireframe.model.Geometry;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.CameraNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.ModelNode;
import ru.nsu.fit.g20204.kuznetsov.wireframe.node.SceneNode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public interface ModelParser {
    static SceneNode fileToScene(File file) {
        try (DataInputStream input = new DataInputStream(new FileInputStream(file))) {
            SceneNode scene = new SceneNode(null);

            scene.setLocalTransformMatrix(streamToMatrix(input));

            CameraNode camera = scene.createCameraNode();
            camera.setLocalTransformMatrix(streamToMatrix(input));

            camera.setNearClippingPlane(input.readDouble());
            camera.setFarClippingPlane(input.readDouble());
            camera.setViewPortWidth(input.readDouble());
            camera.setViewPortHeight(input.readDouble());

            ModelNode modelNode = new ModelNode(scene);
            modelNode.setLocalTransformMatrix(streamToMatrix(input));

            modelNode.setModel(streamToGeometry(input));
            scene.addChild(modelNode);
            scene.setModel(modelNode);

            return scene;
        } catch (IOException e) {
            return null;
        }
    }

    static boolean sceneToFile(SceneNode scene, File file) {

        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(file))) {
            matrixToStream(scene.getLocalTranformMatrix(), output);

            CameraNode camera = scene.getCameraList().get(0);
            matrixToStream(camera.getLocalTranformMatrix(), output);

            output.writeDouble(camera.getNearClippingPlane());
            output.writeDouble(camera.getFarClippingPlane());
            output.writeDouble(camera.getViewPortWidth());
            output.writeDouble(camera.getViewPortHeight());

            ModelNode modelNode = scene.getModel();
            matrixToStream(modelNode.getLocalTranformMatrix(), output);

            geometryToStream(modelNode.getModel(), output);
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    static Geometry streamToGeometry(DataInputStream input) throws IOException {
        List<Vector> vertexList = new ArrayList<>();
        List<Integer> edgeList = new ArrayList<>();

        int vertexCount = input.readInt();

        for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
            Vector vertex = new Vector();
            vertex.x = input.readDouble();
            vertex.y = input.readDouble();
            vertex.z = input.readDouble();
            vertex.w = input.readDouble();
            vertexList.add(vertex);
        }

        int edgeVertexCount = input.readInt();

        for (int vertexIndex = 0; vertexIndex < edgeVertexCount; vertexIndex++) {
            edgeList.add(input.readInt());
        }

        return new Geometry(vertexList, edgeList);
    }

    static void geometryToStream(Geometry geometry, DataOutputStream output) throws IOException {
        List<Vector> vertexList = geometry.getVertexList();
        List<Integer> edgeList = geometry.getEdgeList();

        output.writeInt(vertexList.size());

        for (var vertex: vertexList) {
            output.writeDouble(vertex.x);
            output.writeDouble(vertex.y);
            output.writeDouble(vertex.z);
            output.writeDouble(vertex.w);
        }

        output.writeInt(edgeList.size());

        for (var vertexIndex: edgeList) {
            output.writeInt(vertexIndex);
        }
    }


    static void matrixToStream(Matrix matrix, DataOutputStream output) throws IOException {
        double[][] matrixArray = matrix.matrix;

        for (double[] doubles : matrixArray) {
            for (int j = 0; j < matrixArray[0].length; j++) {
                output.writeDouble(doubles[j]);
            }
        }
    }

    static Matrix streamToMatrix(DataInputStream input) throws IOException {
        double[][] matrixArray = new double[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrixArray[i][j] = input.readDouble();
            }
        }

        return new Matrix(matrixArray);
    }
}
