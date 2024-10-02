package main;

import javax.swing.JFrame;

import algebre.Matrice;
import ui.MainWindow;

public class MatrixTest {

    public static void main(String s[]) {

        // Example 1: 2x2 matrix
        double[][] matrixValues2x2 = {
                {4, 7},
                {2, 6}
        };
        Matrice matrix2x2 = new Matrice(matrixValues2x2);

        // Inverting the 2x2 matrix
        Matrice inverseMatrix2x2 = matrix2x2.inverse(matrix2x2);

        // Check if the matrix is invertible and print the result
        if (inverseMatrix2x2 != null) {
            System.out.println("Inverse of 2x2 matrix:");
            printMatrix(inverseMatrix2x2);
        } else {
            System.out.println("The 2x2 matrix is not invertible.");
        }

        // Example 2: 3x3 matrix
        double[][] matrixValues3x3 = {
                {1, 1, 2},
                {1, 2, 1},
                {2, 1, 1}
        };
        Matrice matrix3x3 = new Matrice(matrixValues3x3);

        // Inverting the 3x3 matrix
        Matrice inverseMatrix3x3 = matrix3x3.inverse(matrix3x3);

        // Check if the matrix is invertible and print the result
        if (inverseMatrix3x3 != null) {
            System.out.println("\nInverse of 3x3 matrix:");
            printMatrix(inverseMatrix3x3);
        } else {
            System.out.println("The 3x3 matrix is not invertible.");
        }

        System.out.println("\n The determinant of 3x3 matrix:\n");
        System.out.println(matrix3x3.computeDeterminant());
    }

    public static void printMatrix(Matrice matrix) {
        for (int i = 0; i < matrix.m.length; i++) {
            for (int j = 0; j < matrix.m[i].length; j++) {
                System.out.printf("%.2f ", matrix.m[i][j]);
            }
            System.out.println();
        }
    }
}
