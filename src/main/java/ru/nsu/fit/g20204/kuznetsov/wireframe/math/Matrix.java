package ru.nsu.fit.g20204.kuznetsov.wireframe.math;

import java.util.Arrays;

import static java.lang.Math.*;
import static java.lang.Math.cos;

public class Matrix {
    private final static int SIZE = 4;
    public double[][] matrix = new double[SIZE][SIZE];
    
    public Matrix() {
        matrix = new double[][] {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
    }
    
    public Matrix(double[][] matrix) {
        if (matrix.length != SIZE || matrix[0].length != SIZE)
            return;
        
        for (int i = 0; i < 4; i++) {
            this.matrix[i] = Arrays.copyOf(matrix[i], SIZE);
        }
    }
    
    public Matrix multiply(double value) {
        double[][] newMatrix = new double[SIZE][SIZE];
        
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < 4; j++) {
                newMatrix[i][j] = this.matrix[i][j] * value;
            }
        }
        
        return new Matrix(newMatrix);
    }

    public Vector multiply(Vector vector, boolean wCorrect) {
        Vector resultVector = new Vector();

        resultVector.x = vector.x * matrix[0][0] + vector.y * matrix[0][1] + vector.z * matrix[0][2] + vector.w * matrix[0][3];
        resultVector.y = vector.x * matrix[1][0] + vector.y * matrix[1][1] + vector.z * matrix[1][2] + vector.w * matrix[1][3];
        resultVector.z = vector.x * matrix[2][0] + vector.y * matrix[2][1] + vector.z * matrix[2][2] + vector.w * matrix[2][3];
        resultVector.w = vector.x * matrix[3][0] + vector.y * matrix[3][1] + vector.z * matrix[3][2] + vector.w * matrix[3][3];

        if (wCorrect) {
            resultVector.correctW();
        }

        return resultVector;
    }

    public Matrix multiply(Matrix other) {
        double[][] newMatrixArray = new double[4][4];
        for (int i = 0; i < 4; i++) {
            Arrays.fill(newMatrixArray[i], 0);
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    newMatrixArray[i][k] += this.matrix[i][j] * other.matrix[j][k];
                }
            }
        }

        return new Matrix(newMatrixArray);
    }

    public Matrix translate(double dx, double dy, double dz) {
        Matrix translationMatrix = Matrix.getTranslationMatrix(dx, dy, dz);

        return translationMatrix.multiply(this);
    }
    
    public Matrix rotate(Vector axis, double angle) {
        Matrix rotationMatrix = Matrix.getRotationMatrix(axis, angle);

        return rotationMatrix.multiply(this);
    }
    
    public Matrix scale(double xScale, double yScale, double zScale) {
        Matrix scaleMatrix = Matrix.getScaleMatrix(xScale, yScale, zScale);

        return scaleMatrix.multiply(this);
    }

    private static Matrix getTranslationMatrix(double dx, double dy, double dz) {
        return new Matrix(new double[][] {{1, 0, 0, dx},
                {0, 1, 0, dy},
                {0, 0, 1, dz},
                {0, 0, 0, 1}});
    }
    
    public static Matrix getScaleMatrix(double xScale, double yScale, double zScale) {
        return new Matrix(new double[][] {{xScale, 0, 0, 0},
                {0, yScale, 0, 0},
                {0, 0, zScale, 0},
                {0, 0, 0, 1}});
    }
    
    public static Matrix getRotationMatrix(Vector axis, double angleDegrees) {
        axis.normalize();
        double x = axis.x;
        double y = axis.y;
        double z = axis.z;
        double angle = angleDegrees * 2 * PI / 360;
        return new Matrix(new double[][] {{cos(angle) + (1 - cos(angle)) * x * x, (1 - cos(angle)) * x * y - sin(angle) * z, (1 - cos(angle)) * x * z + sin(angle) * y, 0},
                {(1 - cos(angle)) * x * y + sin(angle) * z, cos(angle) + (1 - cos(angle)) * y * y, (1 - cos(angle)) * y * z - sin(angle) * x, 0},
                {(1 - cos(angle)) * x * z - sin(angle) * y, (1 - cos(angle)) * y * z + sin(angle) * x, cos(angle) + (1 - cos(angle)) * z * z, 0},
                {0                                      , 0                                          , 0                                    , 1}});
    }

    @Override
    public String toString() {
        return Arrays.deepToString(matrix);
    }
}
