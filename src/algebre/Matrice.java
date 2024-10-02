package algebre;

import java.util.ArrayList;

//matrices carrees
public class Matrice {
	public final double[][] m;
	private final int dimension;
	private double determinant;

	public Matrice(double[][] coefs) {
		dimension = coefs[0].length;
		m = new double[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
            System.arraycopy(coefs[i], 0, m[i], 0, dimension);
		}
	}

	public Matrice identity(int dimension) {
		double[][] identityMatrix = new double[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			identityMatrix[i][i] = 1.0;
		}
		return new Matrice(identityMatrix);
	}

	public Matrice covariance(ArrayList<Vecteur> lv) {
		if (lv == null || lv.isEmpty()) {
			throw new IllegalArgumentException("La liste des vecteurs ne peut pas être vide.");
		}
		int dimension = lv.getFirst().getDimension();
		for (Vecteur v : lv) {
			if (v.getDimension() != dimension) {
				throw new IllegalArgumentException("Tous les vecteurs doivent avoir la même dimension.");
			}
		}

		Vecteur mean = lv.getFirst().esperance(lv);
		double[][] covarianceMatrix = new double[dimension][dimension];

		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				double sum = 0.0;
				for (Vecteur v : lv) {
					sum += (v.get(i) - mean.get(i)) * (v.get(j) - mean.get(j));
				}
				covarianceMatrix[i][j] = sum / lv.size();
			}
		}

		return new Matrice(covarianceMatrix);
	}

	public static Matrice computeAverageMatrixFromMatrixList(ArrayList<Matrice> l) {
		double[][] matrix = new double[l.getFirst().dimension][l.getFirst().dimension];
		for (Matrice matrice : l) {
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[i].length; j++) {
					matrix[i][j] += matrice.m[i][j];
				}
			}

		}
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j]/= l.size();
			}
		}
		return new Matrice(matrix);
	}

	private double get(int i, int j) {
		return this.m[i][j];
	}
	
	public Vecteur getColumn(int k) {
		double[] column = new double[dimension];
		for (int i = 0; i < dimension; i++) {
			column[i] = m[i][k];
		}
		return new Vecteur(column);
	}

	private static void swapRows(double[][] matrix, int row1, int row2) {
		double[] temp = matrix[row1];
		matrix[row1] = matrix[row2];
		matrix[row2] = temp;
	}

	public Matrice inverse(Matrice a) {
		int n = a.dimension;

		double[][] mat = new double[n][n];
		for (int i = 0; i < n; i++) {
			System.arraycopy(a.m[i], 0, mat[i], 0, n);
		}

		Matrice identityMatrix = identity(n);
		double[][] m = identityMatrix.m;

		for (int k = 0; k < n; k++) {
			int p = k;
			for (int i = k + 1; i < n; i++) {
				if (Math.abs(mat[i][k]) > Math.abs(mat[p][k])) {
					p = i;
				}
			}

			if (p != k) {
				swapRows(mat, k, p);
				swapRows(m, k, p);
			}

			if (mat[k][k] == 0) {
				return null;
			}

			double diag = mat[k][k];
			for (int l = 0; l < n; l++) {
				mat[k][l] /= diag;
				m[k][l] /= diag;
			}

			for (int i = 0; i < n; i++) {
				if (i != k) {
					double c = mat[i][k];
					for (int j = 0; j < n; j++) {
						mat[i][j] -= c * mat[k][j];
						m[i][j] -= c * m[k][j];
					}
				}
			}
		}
		return new Matrice(m);
	}

	public Vecteur mult(Vecteur v) {
		if (v.getDimension() != this.dimension) {
			throw new IllegalArgumentException("La dimension du vecteur doit correspondre au nombre de colonnes de la matrice.");
		}

		double[] result = new double[this.dimension];

		for (int i = 0; i < this.dimension; i++) {
			result[i] = 0;
			for (int j = 0; j < this.dimension; j++) {
				result[i] += this.m[i][j] * v.get(j);
			}
		}
		return new Vecteur(result);
	}


	public double computeDeterminant() {
		double[][] mat = new double[this.dimension][this.dimension];
		for (int i = 0; i < this.dimension; i++) {
			System.arraycopy(this.m[i], 0, mat[i], 0, this.dimension);
		}
		this.determinant = gaussianEliminationForDeterminant(mat);
		return determinant;
	}

	private double gaussianEliminationForDeterminant(double[][] matrix) {
		int n = matrix.length;
		double det = 1.0;

		for (int k = 0; k < n; k++) {
			int p = k;
			for (int i = k + 1; i < n; i++) {
				if (Math.abs(matrix[i][k]) > Math.abs(matrix[p][k])) {
					p = i;
				}
			}

			if (matrix[p][k] == 0) {
				return 0.0;
			}

			if (p != k) {
				swapRows(matrix, k, p);
				det *= -1;
			}
			det *= matrix[k][k];

			for (int i = k + 1; i < n; i++) {
				double factor = matrix[i][k] / matrix[k][k];
				for (int j = k; j < n; j++) {
					matrix[i][j] -= factor * matrix[k][j];
				}
			}
		}
		return det;
	}
}
