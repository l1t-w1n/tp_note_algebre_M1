package algebre;

import java.util.ArrayList;

public class Vecteur {
	double coords[];

	// Constructor to initialize a vector with a specified dimension
	public Vecteur(int dimension) {
		coords = new double[dimension];
	}

	// Constructor that initializes a vector with given coordinates
	public Vecteur(double[] bk) {
		coords = new double[bk.length];
		for (int i = 0; i < bk.length; i++)
			coords[i] = bk[i];
	}

	// Copy constructor to create a new vector from an existing one
	public Vecteur(Vecteur v) {
		coords = new double[v.getDimension()];
		for (int i = 0; i < coords.length; i++)
			coords[i] = v.get(i);
	}

	// Calculates the dot product between two vectors
	// Assumes both vectors have the same dimension; no error checking is done
	public double produitScalaire(Vecteur v) {
		double resultat = 0.0;
		for (int i = 0; i < this.coords.length; i++) {
			resultat += this.coords[i] * v.coords[i];
		}
		return resultat;
	}

	// Computes the cosine of the angle between this vector and another vector
	public double cosinus(Vecteur v) {
		return produitScalaire(v) / (this.norme() * v.norme());
	}

	// Calculates the magnitude of the vector using the Euclidean norm
	public double norme() {
		double somme = 0.0;
		for (double coord : this.coords) {
			somme += coord * coord;
		}
		return Math.sqrt(somme);
	}

	// Computes the cross product of two 3D vectors
	// Only defined for 3-dimensional vectors
	public Vecteur produitVectoriel(Vecteur v) {
		if (this.coords.length != 3 || v.coords.length != 3) {
			throw new IllegalArgumentException("Le produit vectoriel est uniquement défini pour des vecteurs en 3 dimensions.");
		}

		double[] result = new double[3];
		result[0] = this.coords[1] * v.coords[2] - this.coords[2] * v.coords[1];
		result[1] = this.coords[2] * v.coords[0] - this.coords[0] * v.coords[2];
		result[2] = this.coords[0] * v.coords[1] - this.coords[1] * v.coords[0];

		return new Vecteur(result);
	}

	// Calculates the mean vector of a list of vectors
	// All vectors in the list must have the same dimension
	public Vecteur esperance(ArrayList<Vecteur> lv) {
		if (lv == null || lv.isEmpty()) {
			throw new IllegalArgumentException("La liste des vecteurs ne peut pas être vide.");
		}

		int dimension = lv.get(0).coords.length;
		for (Vecteur v : lv) {
			if (v.coords.length != dimension) {
				throw new IllegalArgumentException("Tous les vecteurs doivent avoir la même dimension.");
			}
		}

		double[] somme = new double[dimension];

		// Sum the components of all vectors
		for (Vecteur v : lv) {
			for (int i = 0; i < dimension; i++) {
				somme[i] += v.coords[i];
			}
		}

		// Divide by the number of vectors to get the mean
		int nombreVecteurs = lv.size();
		for (int i = 0; i < dimension; i++) {
			somme[i] /= nombreVecteurs;
		}
		return new Vecteur(somme);
	}

	// Getter for the value of the vector at a specific index
	public double get(int i) {
		return coords[i];
	}

	// Setter for the value of the vector at a specific index
	public void setValueForFeature(int i, double d) {
		this.coords[i] = d;
	}

	// Returns the dimension of the vector
	public int getDimension() {
		return this.coords.length;
	}

}
