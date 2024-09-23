package Matrix;

import java.util.Arrays;

public class Matrix {

    private double[][] elements;
    private String[][] elementsStr;
    private int size;

    private int rows,cols;

    public double[][] getElements() {
        return elements;
    }
    public int getSize() {
        return size;
    }

    public Matrix(double[][] elements, int size) {
        this.elements = elements;
        this.size = size;
        this.rows = size;
        this.cols = size;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Matrix(double[][] elements, int rows, int cols) {
        this.elements = elements;
        this.rows = rows;
        this.cols = cols;
    }
    public static Matrix subMatrix(int row, int col, Matrix matrix) {
        double[][] el = new double[matrix.getSize()-1][matrix.getSize()-1];
        for(int i = 0; i < matrix.getSize(); i++) {
            for(int j = 0; j < matrix.getSize(); j++) {
                if(i == row || j == col) continue;
                el[i < row ?  i  : i  - 1][j < col ? j : j - 1] = matrix.getElements()[i][j];
            }
        }
        return new Matrix(el,matrix.getSize()-1);
    }

    public static double determinant(Matrix matrix) {
        double det = 0.0;
        if(matrix.getSize() == 1) {
            det = matrix.getElements()[0][0];
        }
        if(matrix.getSize() == 2) {
            det = matrix.getElements()[0][0] * matrix.getElements()[1][1] - matrix.getElements()[0][1] * matrix.getElements()[1][0];
        }
        if(matrix.getSize() > 2) {
            for (int i = 0; i < matrix.getSize(); i++) {
                det+=Math.pow(-1,i) * matrix.getElements()[0][i] * determinant(subMatrix(0,i,matrix));
            }
        }
        return det;
    }

    public static Matrix transpose(Matrix matrix) {
        double[][] transposed = new double[matrix.getSize()][matrix.getSize()];
        for (int i = 0; i < matrix.getSize(); i++) {
            for (int j = 0; j < matrix.getSize(); j++) {
                transposed[j][i] = matrix.getElements()[i][j];
            }
        }
        return new Matrix(transposed,matrix.getSize());
    }

    public static Matrix cofactorMatrix(Matrix matrix) {
        double[][] minorMatrix = new double[matrix.getSize()][matrix.getSize()];

        for (int i = 0; i < matrix.getSize(); i++) {
            for (int j = 0; j < matrix.getSize(); j++) {
                minorMatrix[i][j] = Math.pow(-1,(i+j)) * determinant(subMatrix(i,j,matrix));
            }
        }
        return new Matrix(minorMatrix,matrix.getSize());
    }

    public static Matrix adjugateMatrix(Matrix matrix) {
        return transpose(cofactorMatrix(matrix));
    }

    public static Matrix multiplyByScalar(Matrix matrix, double scalar) {
        double[][] matrixElements = new double[matrix.getSize()][matrix.getSize()];
        for (int i = 0; i < matrix.getSize(); i++) {
            for (int j = 0; j < matrix.getSize(); j++) {
                matrixElements[i][j] = matrix.getElements()[i][j] * scalar;
            }
        }
        return new Matrix(matrixElements,matrix.getSize());
    }

    public static Matrix multiplyMatrix(Matrix A, Matrix B) {
        double[][] matrixElements = new double[A.getRows()][B.getCols()];
        for (int i = 0; i < A.getRows(); i++) {
            for (int j = 0; j < B.getCols(); j++) {
                for (int k = 0; k < A.getCols(); k++) {
                    matrixElements[i][j] += A.getElements()[i][k] * B.getElements()[k][j];
                }
            }
        }
        return new Matrix(matrixElements,matrixElements.length,matrixElements[0].length);
    }
    public static Matrix inverseMatrix(Matrix matrix) {
        return multiplyByScalar(adjugateMatrix(matrix),1/determinant(matrix));
    }

    public static Matrix solveEquation(Matrix left, Matrix right) {

        if(left.getSize() > 1) return multiplyMatrix(inverseMatrix(left), right);

        double sol = right.getElements()[0][0] / left.getElements()[0][0];
        double[][] mat = new double[1][1];
        mat[0][0] = sol;
        return new Matrix(mat, 1);
    }

    @Override
    public String toString() {
        return "Matrix{" +
                "elements=" + Arrays.deepToString(elements) +
                ", size=" + rows + "x" + cols +
                '}';
    }
}
